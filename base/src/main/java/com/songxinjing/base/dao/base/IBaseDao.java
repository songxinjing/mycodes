package com.songxinjing.base.dao.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;

/**
 * 数据访问基础接口
 * 
 * @author songxinjing
 * 
 */
public interface IBaseDao<T, PK extends Serializable> {

	/**
	 * 新增
	 * 
	 * @param entity
	 *            实体对象
	 * @return 主键值
	 */
	public Serializable save(final T entity);

	/**
	 * 修改
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void update(final T entity);

	/**
	 * 删除单个对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void delete(final T entity);

	/**
	 * 通过主键ID删除
	 * 
	 * @param id
	 *            主键
	 */
	public void delete(final PK id);

	/**
	 * 删除一组对象
	 * 
	 * @param list
	 *            实体对象List
	 */
	public void delete(final Collection<T> list);

	/**
	 * 查询全部对象
	 * 
	 * @return 实体对象List
	 */
	public List<T> find();

	/**
	 * 通过主键ID查询对象.
	 * 
	 * @param id
	 *            主键
	 * @return 实体对象
	 */
	public T findByPK(final PK id);

	/**
	 * 通过含有部分属性的对象查询符合该属性的对象List.
	 * 
	 * @param entity
	 *            模板实体对象
	 * @return 实体对象List
	 */
	public List<T> find(final T entity);
	
	/**
	 * 执行一个HQL查询, 使用预编译方式绑定参数.
	 * 
	 * @param queryString
	 *            预编译HQL
	 * @param values
	 *            占位参数
	 * @return 查询结果List
	 */
	public List<?> find(final String queryString, final Object... values);
	
	/**
	 * 执行一个HQL查询（带In语法参数）.
	 * 
	 * @param queryString
	 *            预编译HQL
	 * @param placeHolder
	 *            占位符（:placeHolder）
	 * @param values
	 *            占位参数
	 * @return 查询结果List
	 */
	public List<?> findIn(final String queryString, String placeHolder, List<?> values);
	
	/**
	 * 按HQL查询指定分页对象列表.
	 * 
	 * @param hql
	 *            预编译的HQL语句
	 * @param from
	 *            当前页的第一条记录行号
	 * @param size
	 *            每页记录数
	 * @param values
	 *            数量可变的参数
	 */
	public List<?> findPage(final String queryString, final int from, final int size, final Object... values);

	
	/**
	 * 执行一个HQL更新或删除, 使用预编译方式绑定参数.
	 * 
	 * @param queryString
	 *            预编译HQL
	 * @param values
	 *            占位参数
	 * @return 操作的记录数
	 */
	public int updOrDel(final String queryString, final Object... values);
	
	/**
	 * 构造一个HQLQuery对象供后续使用.
	 * 
	 * @param queryString
	 *            预编译HQL
	 * @param values
	 *            占位参数
	 */
	public Query createQuery(final String queryString, final Object... values);

	/**
	 * 构造一个Criteria对象供后续使用.
	 * 
	 * @param criterions
	 *            条件
	 */
	public Criteria createCriteria(final Criterion... criterions);
	
	/**
	 * 检查字段取值是否唯一.
	 * 
	 * @param propertyName
	 *            字段名
	 * @param newValue
	 *            字段新值
	 * @param orgValue
	 *            字段旧值
	 * @return true/false
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object orgValue);

}
