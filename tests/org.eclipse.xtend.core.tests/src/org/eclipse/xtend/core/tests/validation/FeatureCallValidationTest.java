/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtend.core.tests.validation;

import static org.eclipse.xtext.xbase.validation.IssueCodes.*;

import java.util.List;

import org.eclipse.xtend.core.tests.AbstractXtendTestCase;
import org.eclipse.xtend.core.xtend.XtendClass;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotationsPackage;
import org.eclipse.xtext.xbase.validation.IssueCodes;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class FeatureCallValidationTest extends AbstractXtendTestCase {

	@Inject
	private ValidationTestHelper helper;

	@Test
	public void testBrokenModel_01() throws Exception {
		XtendClass clazz = clazz("class C { def m(DoesNotExist d) { d.unknown.unknown }}");
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
	}
	
	@Test
	public void testBrokenModel_02() throws Exception {
		XtendClass clazz = clazz("class C { def m(DoesNotExist it) { unknown.unknown }}");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
	}
	
	@Test
	public void testBrokenModel_03() throws Exception {
		XtendClass clazz = clazz("class C { def void m(DoesNotExist d) { m('') }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
	}
	
	@Test
	public void testBrokenModel_04() throws Exception {
		XtendClass clazz = clazz("class C { def void m() { DoesNotExist::unknown }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL, Diagnostic.LINKING_DIAGNOSTIC, "DoesNotExist cannot be resolved to a type.");
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
	}
	
	@Test
	public void testBrokenModel_05() throws Exception {
		XtendClass clazz = clazz("class C { @DoesNotExist(unknown='zonk') def void m() {}}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertError(clazz, XAnnotationsPackage.Literals.XANNOTATION, Diagnostic.LINKING_DIAGNOSTIC, "DoesNotExist cannot be resolved to an annotation type.");
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
	}
	
	@Test
	public void testBrokenModel_06() throws Exception {
		XtendClass clazz = clazz("class C { UnknownType fieldName def void m() { fieldName.unknownOperation }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testBrokenModel_07() throws Exception {
		XtendClass clazz = clazz("class C { UnknownType fieldName = '' }");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}

	@Test
	public void testBrokenModel_08() throws Exception {
		XtendClass clazz = clazz("class C { def UnknownType m1() {} def void m2() { m1.unknownOperation }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testBrokenModel_09() throws Exception {
		XtendClass clazz = clazz("class C { def void m2() { <UnknownType>newArrayList.head.unknownOperation }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testBrokenModel_10() throws Exception {
		XtendClass clazz = clazz("class C { def void m2() { #[ null as UnknownType ].head.unknownOperation }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testBrokenModel_11() throws Exception {
		XtendClass clazz = clazz("class C { def UnknownType m1() {} def void m2() { newArrayList(m1, m1).map[ it.unknownOperation ] }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testBrokenModel_12() throws Exception {
		XtendClass clazz = clazz("class C { def void m() { <UnknownType>newArrayList().map[ it.unknownOperation ] }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertNoError(clazz, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC);
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	// we expect feature linking problems if no explicit receiver was used
	@Test
	public void testBrokenModel_13() throws Exception {
		XtendClass clazz = clazz("class C { def UnknownType m1() {} def void m2() { newArrayList(m1, m1).map[ unknownOperation.doesNotExist ] }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC, "unknownOperation");
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	// we expect feature linking problems if no explicit receiver was used
	@Test
	public void testBrokenModel_14() throws Exception {
		XtendClass clazz = clazz("class C { def void m() { <UnknownType>newArrayList().map[ unknownOperation.doesNotExist ] }}");
		helper.assertNoError(clazz, IssueCodes.INCOMPATIBLE_TYPES);
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL, org.eclipse.xtend.core.validation.IssueCodes.FEATURECALL_LINKING_DIAGNOSTIC, "unknownOperation");
		helper.assertError(clazz, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, "UnknownType cannot be resolved to a type.");
	}
	
	@Test
	public void testLocationForDeclaratorMarker() throws Exception {
		String input = "class C { def void m() { com::acme::Unknown::unknown }}";
		XtendClass clazz = clazz(input);
		List<Issue> issues = helper.validate(clazz);
		assertEquals(String.valueOf(issues), 1, issues.size());
		Issue issue = issues.get(0);
		assertEquals(input.indexOf("com::acme"), issue.getOffset().intValue());
		assertEquals("com::acme::Unknown".length(), issue.getLength().intValue());
		assertEquals(1, issue.getLineNumber().intValue());
	}
	
	/**
	 * see https://bugs.eclipse.org/bugs/show_bug.cgi?id=362240
	 */
	@Test
	public void testBug362240_1() throws Exception {
		XtendClass clazz = clazz("class Bar { def <T> bar(T t) { <Integer>bar(t) }}");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				IssueCodes.INCOMPATIBLE_TYPES, "Type mismatch: cannot convert from T to Integer");
	}
	
	/**
	 * see https://bugs.eclipse.org/bugs/show_bug.cgi?id=362240
	 */
	@Test
	public void testBug362240_2() throws Exception {
		XtendClass clazz = clazz("class Bar<T> { def <X> void bar(X x) { val T t = null; <Integer>bar(t) }}");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				IssueCodes.INCOMPATIBLE_TYPES, "Type mismatch: cannot convert from T to Integer");
	}
	
	/**
	 * see https://bugs.eclipse.org/bugs/show_bug.cgi?id=362240
	 */
	@Test
	public void testBug362240_3() throws Exception {
		XtendClass clazz = clazz("class Bar<T extends Integer> { def <X> void bar(X x) { val T t = null; <Integer>bar(t) }}");
		helper.assertNoErrors(clazz);
	}
	
	@Test 
	public void testBug400653() throws Exception {
		XtendClass clazz = clazz("class X { def void m(String x) { x.m2 } def m2(char c) {}}");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				IssueCodes.INCOMPATIBLE_TYPES, "Type mismatch: cannot convert from String to char");
	}
	
	@Test 
	public void testBug400653_02() throws Exception {
		XtendClass clazz = clazz("class X { def void m(String it) { m2 } def m2(char c) {}}");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				IssueCodes.INCOMPATIBLE_TYPES, "Type mismatch: cannot convert implicit first argument from String to c");
	}
	
	@Test
	public void testBug400989() throws Exception {
		String content = "abstract class Bar<E> extends java.util.AbstractList<E> { protected new() { new Object() => [add && remove] }}";
		XtendClass clazz = clazz(content);
		List<Issue> issues = helper.validate(clazz);
		assertEquals(1, issues.size());
		Issue issue = issues.get(0);
		assertEquals(IssueCodes.INCOMPATIBLE_TYPES, issue.getCode());
		assertTrue(issue.getMessage(), issue.getMessage().contains("cannot convert implicit first argument from Object to E"));
		assertEquals(content.indexOf("add"), issue.getOffset().intValue());
		assertEquals(3, issue.getLength().intValue());
		assertEquals(1, issue.getLineNumber().intValue());
	}
	
	@Test 
	public void testConstructInstanceOfTypeParameter() throws Exception {
		XtendClass clazz = clazz("class X<T> { def m() { new T } }");
		helper.assertError(clazz, XbasePackage.Literals.XCONSTRUCTOR_CALL,
				IssueCodes.ILLEGAL_CLASS_INSTANTIATION, "Cannot instantiate the type parameter T");
	}
	
	@Test 
	public void testThisInStaticContext() throws Exception {
		XtendClass clazz = clazz("class X { static def meth() { this.toString } }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}
	
	@Test 
	public void testThisInInstanceContext() throws Exception {
		XtendClass clazz = clazz("class X { def meth() { this.toString } }");
		helper.assertNoErrors(clazz);
	}
	
	@Test 
	public void testSuperInStaticContext() throws Exception {
		XtendClass clazz = clazz("class X { static def meth() { super.toString } }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}
	
	@Test 
	public void testSuperInInstanceContext() throws Exception {
		XtendClass clazz = clazz("class X { def meth() { super.toString } }");
		helper.assertNoErrors(clazz);
	}
	
	@Test public void testXFeatureCallField1() throws Exception {
		XtendClass clazz = clazz("class X { String foo def meth() { foo.toLowerCase } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXFeatureCallField2() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def meth() { foo.toLowerCase } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXFeatureCallField3() throws Exception {
		XtendClass clazz = clazz("class X { String foo def static meth() { foo.toLowerCase } }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test public void testXFeatureCallField4() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def static meth() { foo.toLowerCase } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallField1() throws Exception {
		XtendClass clazz = clazz("class X { String foo def meth() { new X().foo.toLowerCase } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallField2() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def meth() { new X().foo.toLowerCase } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.INSTANCE_ACCESS_TO_STATIC_MEMBER);
	}

	@Test public void testXMemberFeatureCallField3() throws Exception {
		XtendClass clazz = clazz("class X { String foo def static meth() { new X().foo.toLowerCase } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallField4() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def static meth() { new X().foo.toLowerCase } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.INSTANCE_ACCESS_TO_STATIC_MEMBER);
	}

	@Test public void testXAssignmentField1() throws Exception {
		XtendClass clazz = clazz("class X { String foo def meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXAssignmentField2() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXAssignmentField3() throws Exception {
		XtendClass clazz = clazz("class X { String foo def static meth() { foo = '' } }");
		helper.assertError(clazz, XbasePackage.Literals.XASSIGNMENT,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test public void testXAssignmentField4() throws Exception {
		XtendClass clazz = clazz("class X { static String foo def static meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXFeatureCallOperation1() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def meth() { setFoo('') } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXFeatureCallOperation2() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def meth() { setFoo('') } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXFeatureCallOperation3() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def static meth() { setFoo('') } }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test public void testXFeatureCallOperation4() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def static meth() { setFoo('') } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallOperation1() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def meth() { new X().setFoo('') } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallOperation2() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def meth() { new X().setFoo('') } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.INSTANCE_ACCESS_TO_STATIC_MEMBER);
	}

	@Test public void testXMemberFeatureCallOperation3() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def static meth() { new X().setFoo('') } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallOperation4() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def static meth() { new X().setFoo('') } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.INSTANCE_ACCESS_TO_STATIC_MEMBER);
	}

	@Test public void testXAssignmentOperation1() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test
	public void testXAssignmentOperation2() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test
	public void testXAssignmentOperation3() throws Exception {
		XtendClass clazz = clazz("class X { def setFoo(String x) {} def static meth() { foo = '' } }");
		helper.assertError(clazz, XbasePackage.Literals.XASSIGNMENT,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test
	public void testXAssignmentOperation4() throws Exception {
		XtendClass clazz = clazz("class X { def static setFoo(String x) {} def static meth() { foo = '' } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallImportedMemberExtension1() throws Exception {
		XtendClass clazz = clazz("class X { extension test.ExtensionMethods def meth() { 'foo'.instanceExtension } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallImportedMemberExtension2() throws Exception {
		XtendClass clazz = clazz("class X { extension test.ExtensionMethods def static meth() { 'foo'.instanceExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}
	
	@Test public void testXMemberFeatureCallLocalExtension_01() throws Exception {
		XtendClass clazz = clazz("class X { def meth() { extension var test.ExtensionMethods e = null 'foo'.instanceExtension } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallLocalExtension_02() throws Exception {
		XtendClass clazz = clazz("class X { def static meth() { extension val test.ExtensionMethods e = null 'foo'.instanceExtension } }");
		helper.assertNoErrors(clazz);
	}

	@Test
	@Ignore("static methods from instance extensions are not on the scope")
	public void testXMemberFeatureCallImportedMemberExtension3() throws Exception {
		XtendClass clazz = clazz("class X { extension test.ExtensionMethods def meth() { 'foo'.staticExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test
	@Ignore("static methods from instance extensions are not on the scope")
	public void testXMemberFeatureCallImprotedMemberExtension4() throws Exception {
		XtendClass clazz = clazz("class X { extension test.ExtensionMethods def static meth() { 'foo'.staticExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test public void testXMemberFeatureCallLocalMemberExtension1() throws Exception {
		XtendClass clazz = clazz("class X { def instanceExtension(String foo) { foo + 'inst' } def meth() { 'foo'.instanceExtension } }");
		helper.assertNoErrors(clazz);
	}

	@Test public void testXMemberFeatureCallLocalMemberExtension2() throws Exception {
		XtendClass clazz = clazz("class X { def instanceExtension(String foo) { foo + 'inst' } def static meth() { 'foo'.instanceExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test
	@Ignore("static methods from instance extensions are not on the scope")
	public void testXMemberFeatureCallLocalMemberExtension3() throws Exception {
		XtendClass clazz = clazz("class X { def static staticExtension(String foo) { foo + 'inst' } def meth() { 'foo'.staticExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test
	@Ignore("static methods from instance extensions are not on the scope")
	public void testXMemberFeatureCallLocalMemberExtension4() throws Exception {
		XtendClass clazz = clazz("class X { def static staticExtension(String foo) { foo + 'inst' } def static meth() { 'foo'.staticExtension } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.STATIC_ACCESS_TO_INSTANCE_MEMBER);
	}

	@Test public void testPrivateConstructorCalled() throws Exception {
		XtendClass clazz = clazz("class X { def foo() { new test.Constructor() } }");
		helper.assertError(clazz, XbasePackage.Literals.XCONSTRUCTOR_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.FEATURE_NOT_VISIBLE);
	}

	@Test public void testFeatureCallTypeBounds_0() throws Exception {
		XtendClass clazz = clazz("class X { def <T extends CharSequence> T foo() {} var bar = <Object>foo }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.TYPE_BOUNDS_MISSMATCH);
	}

	@Test public void testFeatureCallTypeBounds_1() throws Exception {
		XtendClass clazz = clazz("class X { def <T extends CharSequence> T foo() {} var bar = <String>foo }");
		helper.assertNoError(clazz, org.eclipse.xtext.xbase.validation.IssueCodes.TYPE_BOUNDS_MISSMATCH);
	}
	
	@Test public void testFeatureCallTypeBounds_2() throws Exception {
		XtendClass clazz = clazz("class X { def <T extends CharSequence, U extends T> T foo() {} var bar = <String, CharSequence>foo }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL,
				org.eclipse.xtext.xbase.validation.IssueCodes.TYPE_BOUNDS_MISSMATCH);
	}
	
	@Test public void testFeatureCallTypeBounds_3() throws Exception {
		XtendClass clazz = clazz("class X { def <T extends CharSequence, U extends T> T foo() {} var bar = <CharSequence, String>foo }");
		helper.assertNoError(clazz, 
				org.eclipse.xtext.xbase.validation.IssueCodes.TYPE_BOUNDS_MISSMATCH);
	}
	
	@Test
	@Ignore("TODO improve error message - shouldn't be Could not resolve reference")
	public void testInvalidReceiverForExtension_01() throws Exception {
		XtendClass clazz = clazz("class X { def void m() { ''.toList } }");
		helper.assertError(clazz, XbasePackage.Literals.XMEMBER_FEATURE_CALL, INCOMPATIBLE_TYPES, "Iterable<Object>", "Object[]", "String", "receiver");
	}
	
	@Test
	@Ignore("TODO improve error message - shouldn't be Could not resolve reference")
	public void testInvalidReceiverForExtension_02() throws Exception {
		XtendClass clazz = clazz("class X { def void m() { var it = '' toList } }");
		helper.assertError(clazz, XbasePackage.Literals.XFEATURE_CALL, INCOMPATIBLE_TYPES, "Iterable<Object>", "Object[]", "String", "first", "argument");
	}
}
