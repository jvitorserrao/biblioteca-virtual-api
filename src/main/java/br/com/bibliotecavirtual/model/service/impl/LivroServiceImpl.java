package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.LivroRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.LivroResponseDTO;
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
import br.com.bibliotecavirtual.model.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final QrCodeClient qrCodeClient;

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

        Livro livro = preencherDto(new Livro(), dto);
        gerarQrCode(livro);
        return LivroResponseDTO.from(livroRepository.save(livro));
    }

    @Override
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = buscarLivro(id);

        if (livroRepository.existsByIsbnAndIdNot(dto.isbn(), id)) {
            throw new RegraNegocioException("Já existe outro livro cadastrado com o ISBN " + dto.isbn());
        }

        preencherDto(livro, dto);
        gerarQrCode(livro);
        return LivroResponseDTO.from(livroRepository.save(livro));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        livroRepository.delete(buscarLivro(id));
    }

    @Override
    @Transactional
    public byte[] buscarQrCode(Long id) {
        Livro livro = buscarLivro(id);

        if (livro.getQrCode() == null) {
            gerarQrCode(livro);
        }

        if (livro.getQrCode() == null) {
            throw new IntegracaoException("Não foi possível gerar o QR Code do livro. Tente novamente mais tarde.");
        }

        return Base64.getDecoder().decode(livro.getQrCode());
    }

    private void gerarQrCode(Livro livro) {
        String conteudo = montarConteudoQrCode(livro);
        String qrCode = qrCodeClient.gerar(conteudo)
                .map(imagem -> Base64.getEncoder().encodeToString(imagem))
                .orElse(null);
        livro.setQrCode(qrCode);
    }

    private String montarConteudoQrCode(Livro livro) {
        StringBuilder conteudo = new StringBuilder()
                .append("Título: ").append(livro.getTitulo()).append("\n")
                .append("Autor: ").append(livro.getAutor().getNome()).append("\n")
                .append("Editora: ").append(livro.getEditora().getNome()).append("\n")
                .append("Ano: ").append(livro.getAnoPublicacao()).append("\n")
                .append("ISBN: ").append(livro.getIsbn());

        if (livro.getLinkAcesso() != null) {
            conteudo.append("\n").append("Acesse: ").append(livro.getLinkAcesso());
        }

        return conteudo.toString();
    }

    private Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com id " + id));
    }

    private Livro preencherDto(Livro livro, LivroRequestDTO dto) {
        if (dto.anoPublicacao() > Year.now().getValue()) {
            throw new RegraNegocioException("O ano de publicação não pode ser maior que o ano atual");
        }

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setNumeroPaginas(dto.numeroPaginas());
        livro.setSinopse(dto.sinopse());
        livro.setLinkAcesso(dto.linkAcesso());
        livro.setAutor(buscarAutor(dto.autorId()));
        livro.setEditora(buscarEditora(dto.editoraId()));
        livro.setCategoria(buscarCategoriaAtiva(dto.categoriaId()));
        return livro;
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
