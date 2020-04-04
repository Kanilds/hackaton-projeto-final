package com.stefanini.servico;

import com.stefanini.dao.PessoaPerfilDao;
import com.stefanini.model.PessoaPerfil;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.stream.Stream;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PessoaPerfilServico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaPerfilDao dao;

    public Stream<PessoaPerfil> buscarPessoaPerfil(Long idPessoa, Long idPerfil) {
        return dao.buscarPessoaPerfil(idPessoa, idPerfil);
    }


}