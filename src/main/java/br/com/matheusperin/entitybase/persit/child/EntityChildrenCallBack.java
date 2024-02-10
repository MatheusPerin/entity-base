package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import java.lang.reflect.Field;
import java.util.Collection;

public interface EntityChildrenCallBack {

    void onChildren(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, Collection<EntityBase> children) throws Exception;

    void onChild(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, EntityBase child) throws Exception;

}
