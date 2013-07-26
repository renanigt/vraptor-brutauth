package br.com.caelum.brutauth.auth.handlers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;

public class HandlerSearcherTest {

	private AccessNotPermitedHandler defaultHandler;
	private RuleSpecificHandler ruleSpecificHandler;
	private Container container;
	private ResourceMethodSpecificHandler resourceSpecificHandler;
	private DefaultResourceMethod handledMethod;
	private DefaultResourceMethod notHandledMethod;

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException{
		container = mock(Container.class);
		
		defaultHandler = new AccessNotPermitedHandler(null);
		when(container.instanceFor(AccessNotPermitedHandler.class)).thenReturn(defaultHandler);
		
		ruleSpecificHandler = new RuleSpecificHandler();
		when(container.instanceFor(RuleSpecificHandler.class)).thenReturn(ruleSpecificHandler);
		
		resourceSpecificHandler = new ResourceMethodSpecificHandler();
		when(container.instanceFor(ResourceMethodSpecificHandler.class)).thenReturn(resourceSpecificHandler);
		
		handledMethod = new DefaultResourceMethod(null, TestController.class.getMethod("handled"));
		notHandledMethod = new DefaultResourceMethod(null, TestController.class.getMethod("notHandled"));
		
	}
	
	@Test
	public void should_get_default_handler_if_class_doesnt_contains_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		RuleWithoutSpecificHandlers rule = new RuleWithoutSpecificHandlers();
		assertEquals(defaultHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_get_specific_handler_of_rule() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		RuleHandler handler = handlerSearcher.getHandler(rule);
		assertEquals(ruleSpecificHandler, handler);
	}
	
	@Test
	public void should_ignore_rule_handler_if_resource_method_has_handledby_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledMethod);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceSpecificHandler, handlerSearcher.getHandler(rule));
	}

	public class RuleWithoutSpecificHandlers implements BrutauthRule{
	}
	
	@HandledBy(RuleSpecificHandler.class)
	public class RuleWithSpecificHandlers implements BrutauthRule{
	}
	
	public class RuleSpecificHandler implements RuleHandler{
		@Override
		public boolean handle(boolean isAllowed) {
			return false;
		}
	}

	public class ResourceMethodSpecificHandler implements RuleHandler{
		@Override
		public boolean handle(boolean isAllowed) {
			return false;
		}
	}
	
	public class TestController{
		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {
		}

		public void notHandled() {
		}
	}
}

