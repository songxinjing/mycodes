package com.songxinjing.base.service.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.songxinjing.base.dao.base.BaseDao;

/**
 * 业务服务对象基础实现类
 * 
 * @author songxinjing
 * 
 */
public abstract class BaseService<T, PK extends Serializable> {

	protected BaseDao<T, PK> dao;

	/**
	 * 设置dao实例
	 * 
	 * @param dao
	 */
	protected void setDao(BaseDao<T, PK> dao) {
		this.dao = dao;
	}

	/**
	 * 新增
	 * 
	 * @param entity
	 *            实体对象
	 * @return 主键值
	 */
	public Serializable save(final T entity) {
		return dao.save(entity);
	}

	/**
	 * 修改
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void update(final T entity) {
		dao.update(entity);
	}

	/**
	 * 删除单个对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void delete(final T entity) {
		dao.delete(entity);
	}

	/**
	 * 删除一组对象
	 * 
	 * @param list
	 *            实体对象List
	 */
	public void delete(final Collection<T> entities) {
		dao.delete(entities);
	}

	/**
	 * 查询全部对象
	 * 
	 * @return 实体对象List
	 */
	public List<T> find() {
		return dao.find();
	}

	/**
	 * 通过主键ID查询对象.
	 * 
	 * @param id
	 *            主键
	 * @return 实体对象
	 */
	public T find(final PK id) {
		return dao.find(id);
	}

	/**
	 * 通过主键ID删除对象.
	 * 
	 * @param id
	 *            主键
	 */
	public void delete(final PK id) {
		delete(find(id));
	}

	/**
	 * 通过含有部分属性的对象查询符合该属性的对象List.
	 * 
	 * @param entity
	 *            模板实体对象
	 * @return 实体对象List
	 */
	public List<T> find(final T entity) {
		return dao.find(entity);
	}

	/**
	 * HQL查询
	 * 
	 * @param queryString
	 * @param values
	 * @return
	 */
	public List<T> findHql(final String hql, final Object... values) {
		return dao.findHql(hql);
	}

	/**
	 * 分页查询
	 * 
	 * @param from
	 * @param size
	 * @return
	 */
	public List<T> findPage(String hql, int from, int size) {
		return dao.findPage(hql, from, size);
	}

}
