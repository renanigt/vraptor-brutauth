package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.interceptors.BrutauthRuleInterceptorTest.TrueCustomRule;


@CustomBrutauthRules(MyCustomRule.class)
@SimpleBrutauthRules(MySimpleBiggerThanZeroRule.class)
@AccessLevel(ControllerWithRules.ACCESS_LEVEL)
public class ControllerWithRules{
	public static final long ACCESS_LEVEL = 100;

	@Public
	public void publicMethod(){
	}
	
	public void methodWithoutRules(){
	}
	
	@CustomBrutauthRules(TrueCustomRule.class)
	@SimpleBrutauthRules(AnotherSimpleRule.class)
	@AccessLevel(1000)
	public void methodWithRules(){
	}
}
