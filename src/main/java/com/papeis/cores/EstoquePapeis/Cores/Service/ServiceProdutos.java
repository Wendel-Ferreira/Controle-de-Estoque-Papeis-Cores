package com.papeis.cores.EstoquePapeis.Cores.Service;

import com.papeis.cores.EstoquePapeis.Cores.Model.EntityProdutos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProdutos {

    @Autowired
    private RepositoryJPA repositoryJPA;

    public EntityProdutos SaveProduct(EntityProdutos entityProdutos) {
        return repositoryJPA.save(entityProdutos);
    }

   public List<EntityProdutos> findAllProdutos() {
       return repositoryJPA.findAll();
   }

    public void deleteProduct(Integer id) {
        try {
            repositoryJPA.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Produto não encontrado para o ID: " + id, e);
        }
    }
    public void existsById(Integer id){
       repositoryJPA.existsById(id);
    }

    public EntityProdutos findById(Integer id) {
        return repositoryJPA.findById(id).orElse(null);
    }

}



