package com.songxinjing.base.dao.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.songxinjing.base.util.ReflectionUtil;

/**
 * 数据访问对象基础实现类
 * 
 * @author songxinjing
 * 
 */
public abstract class BaseDao<T, PK extends Serializable> extends HibernateDaoSupport implements IBaseDao<T, PK> {

	protected Class<T> entityClass;

	public BaseDao() {
		this.entityClass = ReflectionUtil.getSuperClassGenricType(getClass(), 0);
	}

	@Autowired
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Serializable save(final T entity) {
		Assert.notNull(entity);
		return this.getHibernateTemplate().save(entity);
	}

	@Override
	public void update(final T entity) {
		Assert.notNull(entity);
		this.getHibernateTemplate().update(entity);
	}

	@Override
	public void delete(final T entity) {
		Assert.notNull(entity);
		this.getHibernateTemplate().delete(entity);
	}

	@Override
	public void delete(final PK id) {
		Assert.notNull(id);
		this.delete(this.findByPK(id));
	}

	@Override
	public void delete(final Collection<T> entities) {
		Assert.notNull(entities);
		this.getHibernateTemplate().deleteAll(entities);
	}

	@Override
	public List<T> find() {
		return this.getHibernateTemplate().loadAll(entityClass);
	}

	@Override
	public T findByPK(final PK id) {
		Assert.notNull(id);
		return this.getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public List<T> find(final T entity) {
		Assert.notNull(entity);
		return this.getHibernateTemplate().findByExample(entity);
	}

	@Override
	public List<?> find(final String queryString, final Object... values) {
		Assert.hasText(queryString);
		return this.getHibernateTemplate().find(queryString, values);
	}
	
	@Override
	public List<?> findIn(final String queryString, String placeHolder, List<?> values) {
		Assert.hasText(queryString);
		Assert.hasText(placeHolder);
		return this.getHibernateTemplate().findByNamedParam(queryString, placeHolder, values);
	}
	
	@Override
	public List<?> findPage(final String queryString, final int from, final int size, final Object... values) {
		Assert.hasText(queryString);
		return createQuery(queryString, values).setFirstResult(from).setMaxResults(size).list();
	}

	@Override
	public int updOrDel(final String queryString, final Object... values) {
		Assert.hasText(queryString);
		return this.getHibernateTemplate().bulkUpdate(queryString, values);
	}
	
	@Override
	public Query createQuery(String sql, Object... values) {
		Assert.hasText(sql);
		Query query = this.currentSession().createQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	@Override
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = this.currentSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@Override
	public boolean isPropertyUnique(String propertyName, Object newValue, Object orgValue) {
		Assert.hasText(propertyName);
		if (newValue == null || newValue.equals(orgValue)) {
			return true;
		}
		int num = createCriteria(Restrictions.eq(propertyName, newValue)).list().size();
		return (num == 0);
	}

}
