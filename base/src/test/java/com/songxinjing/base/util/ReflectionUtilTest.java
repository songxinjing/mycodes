package com.songxinjing.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilTest {
	
	protected static final Logger logger = LogManager.getLogger(ReflectionUtilTest.class);

	@Test
	public void getAndSetFieldValue() {
		TestBean bean = new TestBean();
		// 无需getter函数, 直接读取privateField
		Assert.assertEquals(1, ReflectionUtil.getFieldValue(bean, "privateField"));
		// 绕过将publicField+1的getter函数,直接读取publicField的原始值
		Assert.assertEquals(1, ReflectionUtil.getFieldValue(bean, "publicField"));

		bean = new TestBean();
		// 无需setter函数, 直接设置privateField
		ReflectionUtil.setFieldValue(bean, "privateField", 2);
		Assert.assertEquals(2, bean.inspectPrivateField());

		// 绕过将publicField+1的setter函数,直接设置publicField的原始值
		ReflectionUtil.setFieldValue(bean, "publicField", 2);
		Assert.assertEquals(2, bean.inspectPublicField());
		try{
			ReflectionUtil.getFieldValue(bean, "notExist");
			ReflectionUtil.setFieldValue(bean, "notExist", 2);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@Test
	public void getSuperClassGenricType() {
		// 获取第1，2个泛型类型
		Assert.assertEquals(String.class, ReflectionUtil.getSuperClassGenricType(TestBean.class, 0));
		Assert.assertEquals(Long.class, ReflectionUtil.getSuperClassGenricType(TestBean.class, 1));

		// 定义父类时无泛型定义
		Assert.assertNull(ReflectionUtil.getSuperClassGenricType(TestBean2.class, 0));

		// 无父类定义
		Assert.assertNull(ReflectionUtil.getSuperClassGenricType(TestBean3.class, 0));
	}

	public static class ParentBean<T, ID> {
	}

	public static class TestBean extends ParentBean<String, Long> {

		/** 没有getter/setter的field */
		private int privateField = 1;
		/** 有getter/setter的field */
		private int publicField = 1;

		// 通过getter方法会比属性值+1
		public int getPublicField() {
			return publicField + 1;
		}

		// 通过setter方法会比属性值+1
		public void setPublicField(int publicField) {
			this.publicField = publicField + 1;
		}

		public int inspectPrivateField() {
			return privateField;
		}

		public int inspectPublicField() {
			return publicField;
		}
	}

	@SuppressWarnings("rawtypes")
	public static class TestBean2 extends ParentBean {
	}

	public static class TestBean3 {

		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
