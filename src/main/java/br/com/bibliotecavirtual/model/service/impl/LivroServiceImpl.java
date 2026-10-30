package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.LivroRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.LivroResponseDTO;
import br.com.bibliotecavirtual.client.ArmazenamentoClient;
import br.com.bibliotecavirtual.client.QrCodeClient;
import br.com.bibliotecavirtual.model.entity.Autor;
import br.com.bibliotecavirtual.model.entity.Categoria;
import br.com.bibliotecavirtual.model.entity.Editora;
import br.com.bibliotecavirtual.model.entity.Livro;
import br.com.bibliotecavirtual.model.exception.IntegracaoException;
import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import br.com.bibliotecavirtual.model.repository.AutorRepository;
import br.com.bibliotecavirtual.model.repository.CategoriaRepository;
import br.com.bibliotecavirtual.model.repository.EditoraRepository;
import br.com.bibliotecavirtual.model.repository.LivroRepository;
import br.com.bibliotecavirtual.model.repository.MaterialDigitalRepository;
import br.com.bibliotecavirtual.model.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final CategoriaRepository categoriaRepository;
    private final MaterialDigitalRepository materialDigitalRepository;
    private final QrCodeClient qrCodeClient;
    private final ArmazenamentoClient armazenamentoClient;

    @Override
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listar() {
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        return LivroResponseDTO.from(buscarLivro(id));
    }

    @Override
    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        if (livroRepository.existsByIsbn(dto.isbn())) {
            throw new RegraNegocioException("Já existe um livro cadastrado com o ISBN " + dto.isbn());
        }

        Livro livro = preencherDto(Livro.builder(), dto);
        return LivroResponseDTO.from(livroRepository.save(livro));
    }

    @Override
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livroAtual = buscarLivro(id);

        if (livroRepository.existsByIsbnAndIdNot(dto.isbn(), id)) {
            throw new RegraNegocioException("Já existe outro livro cadastrado com o ISBN " + dto.isbn());
        }

        Livro livro = preencherDto(livroAtual.toBuilder(), dto);
        return LivroResponseDTO.from(livroRepository.save(livro));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarLivro(id);

        if (materialDigitalRepository.existsByLivroId(id)) {
            throw new RegraNegocioException("Não é possível excluir o livro " + livro.getTitulo() + " pois existem materiais digitais vinculados a ele");
        }

        livroRepository.delete(livro);

        if (livro.getArquivoUrl() != null) {
            armazenamentoClient.remover(livro.getArquivoUrl());
        }
    }

    @Override
    @Transactional
    public byte[] buscarQrCode(Long id) {
        Livro livro = buscarLivro(id);

        if (livro.getArquivoUrl() == null) {
            throw new RecursoNaoEncontradoException("O livro " + livro.getTitulo() + " ainda não possui PDF. Envie o arquivo para gerar o QR Code");
        }

        if (livro.getQrCode() == null) {
            gerarQrCode(livro);
        }

        if (livro.getQrCode() == null) {
            throw new IntegracaoException("Não foi possível gerar o QR Code do livro. Tente novamente mais tarde.");
        }

        return Base64.getDecoder().decode(livro.getQrCode());
    }

    @Override
    @Transactional
    public LivroResponseDTO enviarArquivo(Long id, MultipartFile arquivo) {
        Livro livro = buscarLivro(id);
        byte[] conteudo = lerPdf(arquivo);

        String url = armazenamentoClient.enviar("livro-" + id + ".pdf", conteudo, MediaType.APPLICATION_PDF);

        if (!url.equals(livro.getArquivoUrl()) || livro.getQrCode() == null) {
            livro.setArquivoUrl(url);
            gerarQrCode(livro);
        }

        return LivroResponseDTO.from(livroRepository.save(livro));
    }

    @Override
    @Transactional
    public void removerArquivo(Long id) {
        Livro livro = buscarLivro(id);

        if (livro.getArquivoUrl() == null) {
            throw new RecursoNaoEncontradoException("O livro " + livro.getTitulo() + " não possui arquivo enviado");
        }

        armazenamentoClient.remover(livro.getArquivoUrl());
        livro.setArquivoUrl(null);
        livro.setQrCode(null);
        livroRepository.save(livro);
    }

    private byte[] lerPdf(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new RegraNegocioException("Envie um arquivo PDF");
        }

        byte[] conteudo;
        try {
            conteudo = arquivo.getBytes();
        } catch (IOException ex) {
            throw new RegraNegocioException("Não foi possível ler o arquivo enviado");
        }

        String assinatura = new String(conteudo, 0, Math.min(4, conteudo.length), StandardCharsets.US_ASCII);
        if (!assinatura.equals("%PDF")) {
            throw new RegraNegocioException("Apenas arquivos PDF são aceitos");
        }

        return conteudo;
    }

    private void gerarQrCode(Livro livro) {
        String qrCode = qrCodeClient.gerar(livro.getArquivoUrl())
                .map(imagem -> Base64.getEncoder().encodeToString(imagem))
                .orElse(null);
        livro.setQrCode(qrCode);
    }

    private Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com id " + id));
    }

    private Livro preencherDto(Livro.LivroBuilder builder, LivroRequestDTO dto) {
        if (dto.anoPublicacao() > Year.now().getValue()) {
            throw new RegraNegocioException("O ano de publicação não pode ser maior que o ano atual");
        }

        return builder
                .titulo(dto.titulo())
                .isbn(dto.isbn())
                .anoPublicacao(dto.anoPublicacao())
                .numeroPaginas(dto.numeroPaginas())
                .sinopse(dto.sinopse())
                .linkAcesso(dto.linkAcesso())
                .autor(buscarAutor(dto.autorId()))
                .editora(buscarEditora(dto.editoraId()))
                .categoria(buscarCategoriaAtiva(dto.categoriaId()))
                .build();
    }

    private Autor buscarAutor(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado com id " + id));
    }

    private Editora buscarEditora(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Editora não encontrada com id " + id));
    }

    private Categoria buscarCategoriaAtiva(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id " + id));

        if (!categoria.getAtiva()) {
            throw new RegraNegocioException("A categoria " + categoria.getNome() + " está inativa e não pode receber livros");
        }

        return categoria;
    }
}
