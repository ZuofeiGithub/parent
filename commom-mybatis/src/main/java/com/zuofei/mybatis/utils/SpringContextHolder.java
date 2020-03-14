package com.zuofei.mybatis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);

	private static ApplicationContext applicationContext = null;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	public static String getRootRealPath() {
		String rootRealPath = "";
		try {
			rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			LOGGER.warn("获取系统根目录失败");
		}
		return rootRealPath;
	}

	public static String getResourceRootRealPath() {
		String rootRealPath = "";
		try {
			rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			LOGGER.warn("获取资源根目录失败");
		}
		return rootRealPath;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		LOGGER.info("注入ApplicationContext到SpringContextHolder:{}", applicationContext);

		if (SpringContextHolder.applicationContext != null) {
			LOGGER.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}",
					SpringContextHolder.applicationContext);
		}

		SpringContextHolder.applicationContext = applicationContext; // NOSONAR
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Assert.isTrue(applicationContext != null,
				"applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}

	public static BeanDefinitionBuilder getBeanDefinitionBuilder(@SuppressWarnings("rawtypes") Class clazz) {
		return BeanDefinitionBuilder.genericBeanDefinition(clazz);
	}

	/**
	 * 2019-5-30 主动向Spring容器中注册bean
	 *
	 * @param name
	 *            BeanName
	 * @param clazz
	 *            注册的bean的类性
	 * @param args
	 *            构造方法的必要参数，顺序和类型要求和clazz中定义的一致
	 * @param <T>
	 * @return 返回注册到容器中的bean对象
	 */
	public static <T> T setBean(String name, Class<T> clazz,
			Object... args) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		for (Object arg : args) {
			beanDefinitionBuilder.addConstructorArgValue(arg);
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
		beanFactory.registerBeanDefinition(name, beanDefinition);
		return applicationContext.getBean(name, clazz);
	}
}