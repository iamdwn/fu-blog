package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class RoleCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RoleEntity> getRole(UserEntity user){
        StringBuilder sql = new StringBuilder().append("SELECT swp.role.name as name FROM swp.user join swp.user_role on swp.user.id = swp.user_role.users_id join swp.role on swp.role.id = swp.user_role.roles_id");
        sql.append(" Where 1=1");
        if(user.getUsername() != null){
            sql.append(" and username = :username");
        }
        NativeQuery<RoleEntity> query = ((Session) entityManager.getDelegate()) .createNativeQuery(sql.toString());

        if(user.getUsername() != null){
            query.setParameter("username", user.getUsername());
        }
        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(RoleEntity.class));
        return query.list();
    }
}
