package com.papeis.cores.EstoquePapeis.Cores.Service;

import com.papeis.cores.EstoquePapeis.Cores.Model.EntityProdutos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryJPA  extends JpaRepository<EntityProdutos,Integer> {
}
