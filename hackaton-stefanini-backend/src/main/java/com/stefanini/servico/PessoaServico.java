package com.stefanini.servico;

import com.stefanini.dao.PessoaDao;
import com.stefanini.dao.PessoaPerfilDao;
import com.stefanini.exception.NegocioException;
import com.stefanini.model.Endereco;
import com.stefanini.model.Perfil;
import com.stefanini.model.Pessoa;
import com.stefanini.model.PessoaPerfil;

import javax.ejb.*;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 *
 * Classe de servico, as regras de negocio devem estar nessa classe
 *
 * @author joaopedromilhome
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PessoaServico implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaDao pessoaDao;

    @Inject
    private PessoaPerfilDao pessoaPerfilDao;

    @Inject
    private PessoaPerfilServico pessoaPerfilServico;

    @Inject
    private EnderecoServico enderecoServico;

    /**
     * Salvar os dados de uma Pessoa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Pessoa salvar(@Valid Pessoa pessoa) {
        List<Endereco> enderecos = new ArrayList<>(pessoa.getEnderecos());
        List<Perfil> perfis = new ArrayList<>(pessoa.getPerfils());

        pessoa.getEnderecos().clear();
        pessoa.getPerfils().clear();

        if(nonNull(pessoa.getImagem()))
            pessoa.setImagem(decodeToImage(pessoa.getImagem()));

        Pessoa pessoaSalva = pessoaDao.salvar(pessoa);
        pessoaDao.daoFlush();

        for (Endereco enderecoSalvo : enderecos) {
            enderecoSalvo.setIdPessoa(pessoaSalva.getId());
            enderecoServico.salvar(enderecoSalvo);
        }

        perfis.forEach(perfil -> {
            pessoaPerfilDao.salvar(new PessoaPerfil(perfil, pessoaSalva));
        });

        return pessoaSalva;
    }
    /**
     * Validando se existe pessoa com email
     */
    public Boolean validaSeExisteEmailCadastrado(@Valid Pessoa pessoa){
        Optional<Pessoa> pessoaQuePossuiEmail = pessoaDao.buscarPessoaPorEmail(pessoa.getEmail());
        return pessoaQuePossuiEmail.isPresent();
    }

    /**
     * Atualizar o dados de uma pessoa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Pessoa atualizar(@Valid Pessoa pessoa) {
        pessoa.setImagem(decodeToImage(pessoa.getImagem())); ;
        return pessoaDao.atualizar(pessoa);
    }

    /**
     * Remover uma pessoa pelo id
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void remover(@Valid Long id) throws NegocioException {
        if(pessoaPerfilServico.buscarPessoaPerfil(id,null).count() == 0){
            pessoaDao.remover(id);
            return;
        }
        throw new NegocioException("Não foi possivel remover a pessoa");
    }

    /**
     * Buscar uma lista de Pessoa
     */
    public Optional<List<Pessoa>> getList() {
        return pessoaDao.getList();
    }


    public Optional<List<Pessoa>> obterPessoaCheia() {
        return pessoaDao.pessoasCheias();
    }

    public String decodeToImage(String imagem ) {
        imagem = imagem.split(",")[1];

        String url = "C:\\Users\\pedro\\Desktop\\Hackathon\\Hackathon-Stefanini-backend\\src\\imagens";
        String url2 = "\\imagem"+ Math.random() + ".jpg";

        BufferedImage image = null;
        byte[] imageByte;
        try {

            imageByte = Base64.getDecoder().decode(imagem.getBytes());

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            ImageIO.write((RenderedImage)image, "jpg", new File(url+url2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url + url2 ;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileInputStream urlImg (String localImg){

        String local = "C:\\Users\\pedro\\Desktop\\Hackathon\\Hackathon-Api-Estatico\\src\\app\\imagens"+localImg;

        FileInputStream file = null;

        try{
            file = new FileInputStream(local);
        }catch (FileNotFoundException e){
            e.printStackTrace();}

        return file;
    }





    /**
     * Buscar uma Pessoa pelo ID
     */
    public Optional<Pessoa> encontrar(Long id) {


        Optional<Pessoa> pessoa = pessoaDao.encontrar(id);

        if (pessoa.get().getImagem() != null){

            String urlPath = "http://localhost:8080/treinamento/api/pessoas/imagem/imagem0.";
            String local = pessoa.get().getImagem();

            String[] cocatenar = local.split(Pattern.quote("."));

            pessoa.get().setImagem(urlPath + cocatenar[1] + "." + cocatenar[2]);

        }

        return pessoa;
    }


}