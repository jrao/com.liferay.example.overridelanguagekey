package com.liferay.example.overridelanguagekey;

import com.liferay.portal.kernel.util.AggregateResourceBundleLoader;
import com.liferay.portal.kernel.util.CacheResourceBundleLoader;
import com.liferay.portal.kernel.util.ClassResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoader;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author jrao
 */
@Component(
	immediate = true,
	property = {
		"bundle.symbolic.name=com.liferay.blogs.web",
		"resource.bundle.base.name=content.Language",
		"servlet.context.name=blogs-web"
	}
)
public class ResourceBundleLoaderComponent implements ResourceBundleLoader {

	@Override
	public ResourceBundle loadResourceBundle(String languageId) {
		return _resourceBundleLoader.loadResourceBundle(languageId);
	}

	/*
	 * Added this method to resolve this runtime error:
	 * 
	 * 2018-02-02 19:03:59.866 ERROR [http-nio-8080-exec-9][LiferayMethodExceptionEventHandler:54] Unable to execute method processMax {exception=com.liferay.portal.kernel.portlet.PortletContainerException: com.liferay.portal.kernel.portlet.PortletContainerException: javax.servlet.ServletException: java.lang.AbstractMethodError: Method com/liferay/example/overridelanguagekey/ResourceBundleLoaderComponent.loadResourceBundle(Ljava/util/Locale;)Ljava/util/ResourceBundle; is abstract, className=com.liferay.portal.layoutconfiguration.util.velocity.TemplateProcessor}
	 * com.liferay.portal.kernel.portlet.PortletContainerException: com.liferay.portal.kernel.portlet.PortletContainerException: javax.servlet.ServletException: java.lang.AbstractMethodError: Method com/liferay/example/overridelanguagekey/ResourceBundleLoaderComponent.loadResourceBundle(Ljava/util/Locale;)Ljava/util/ResourceBundle; is abstract
	 */
	public ResourceBundle loadResourceBundle(Locale locale) {
		return _resourceBundleLoader.loadResourceBundle(locale.toString());
	}

	@Reference(target = "(&(bundle.symbolic.name=com.liferay.blogs.web)(!(component.name=com.liferay.blade.samples.hook.resourcebundle.ResourceBundleLoaderComponent)))")
	public void setResourceBundleLoader(ResourceBundleLoader resourceBundleLoader) {

		_resourceBundleLoader = new AggregateResourceBundleLoader(
				new CacheResourceBundleLoader(new ClassResourceBundleLoader("content.Language",
						ResourceBundleLoaderComponent.class.getClassLoader())),
				resourceBundleLoader);
	}

	private AggregateResourceBundleLoader _resourceBundleLoader;

}