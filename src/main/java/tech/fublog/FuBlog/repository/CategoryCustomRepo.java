package tech.fublog.FuBlog.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> getCategory(UserEntity user){
        StringBuilder sql = new StringBuilder().append("SELECT swp.category.category_name as name FROM swp.user join swp.user_category on swp.user.id = swp.user_category.users_id join swp.category on swp.category.id = swp.user_category.categories_id");
        sql.append(" Where 1=1");
        if(user.getUsername() != null){
            sql.append(" and username = :username");
        }
        NativeQuery<CategoryEntity> query = ((Session) entityManager.getDelegate()) .createNativeQuery(sql.toString());

        if(user.getUsername() != null){
            query.setParameter("username", user.getUsername());
        }
        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(CategoryEntity.class));
        return query.list();
    }
}
