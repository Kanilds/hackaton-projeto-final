package com.stefanini.dao;

import com.stefanini.dao.abstracao.GenericDao;
import com.stefanini.model.Pessoa;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * O Unico objetivo da Dao 
 * @author joaopedromilhome
 *
 */
public class PessoaDao extends GenericDao<Pessoa, Long> {
	String sql = " SELECT DISTINCT p FROM Pessoa p LEFT JOIN FETCH p.enderecos enderecos LEFT JOIN FETCH p.perfils perfils ";


	public PessoaDao() {
		super(Pessoa.class);
	}

	/**
	 * Efetuando busca de Pessoa por email
	 * @param email
	 * @return
	 */
	public Optional<Pessoa> buscarPessoaPorEmail(String email){
		TypedQuery<Pessoa> q2 =
				entityManager.createQuery(" select p from Pessoa p where p.email=:email", Pessoa.class);
		q2.setParameter("email", email);
		return q2.getResultStream().findFirst();
	}

	public Optional<List<Pessoa>> pessoasCheias(){
		TypedQuery<Pessoa> q = entityManager.createQuery(sql + " ORDER BY p.nome", Pessoa.class);
		return Optional.ofNullable(q.getResultStream().collect(Collectors.toList()));
	}

	@Override
	public Optional<Pessoa> encontrar(Long id) {
		TypedQuery<Pessoa> q = entityManager.createQuery(sql + " WHERE p.id = :id", Pessoa.class);
		q.setParameter("id", id);
		return q.getResultStream().findFirst();
	}
}
