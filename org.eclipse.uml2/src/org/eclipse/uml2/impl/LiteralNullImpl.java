/*
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - initial API and implementation
 *
 * $Id: LiteralNullImpl.java,v 1.1 2009-03-04 23:06:39 andrew Exp $
 */
package org.eclipse.uml2.impl;

import java.util.Collection;

import java.util.Map;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.LiteralNull;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateParameter;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.UML2Package;
import org.eclipse.uml2.VisibilityKind;

import org.eclipse.uml2.internal.operation.LiteralNullOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Null</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class LiteralNullImpl extends LiteralSpecificationImpl implements LiteralNull {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) IBM Corporation and others."; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteralNullImpl() {
		super();
		if (eAdapters().size() == 0)
			eAdapters().add(com.intrinsarc.notifications.GlobalNotifier.getSingleton());
		if (LiteralNullImpl.class.equals(getClass()) && org.eclipse.emf.common.util.EMFOptions.CREATE_LISTS_LAZILY_FOR_GET)
			com.intrinsarc.notifications.GlobalNotifier.getSingleton().notifyChanged(new org.eclipse.emf.common.notify.impl.NotificationImpl(-1, null, this));
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UML2Package.eINSTANCE.getLiteralNull();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isComputable() {
		return LiteralNullOperations.isComputable(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNull() {
		return LiteralNullOperations.isNull(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case UML2Package.LITERAL_NULL__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
					return ((InternalEList)getTemplateBindings()).basicAdd(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
					if (ownedTemplateSignature != null)
						msgs = ((InternalEObject)ownedTemplateSignature).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE, null, msgs);
					return basicSetOwnedTemplateSignature((TemplateSignature)otherEnd, msgs);
				case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
					return ((InternalEList)getClientDependencies()).basicAdd(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
					if (templateParameter != null)
						msgs = ((InternalEObject)templateParameter).eInverseRemove(this, UML2Package.TEMPLATE_PARAMETER__PARAMETERED_ELEMENT, TemplateParameter.class, msgs);
					return basicSetTemplateParameter((TemplateParameter)otherEnd, msgs);
				case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, UML2Package.LITERAL_NULL__OWNING_PARAMETER, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case UML2Package.LITERAL_NULL__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__OWNED_COMMENT:
					return ((InternalEList)getOwnedComments()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPE_VALUES:
					return ((InternalEList)getAppliedBasicStereotypeValues()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
					return ((InternalEList)getTemplateBindings()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
					return basicSetOwnedTemplateSignature(null, msgs);
				case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
					return ((InternalEList)getClientDependencies()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__NAME_EXPRESSION:
					return basicSetNameExpression(null, msgs);
				case UML2Package.LITERAL_NULL__OWNED_ANONYMOUS_DEPENDENCIES:
					return ((InternalEList)getOwnedAnonymousDependencies()).basicRemove(otherEnd, msgs);
				case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
					return basicSetTemplateParameter(null, msgs);
				case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
					return eBasicSetContainer(null, UML2Package.LITERAL_NULL__OWNING_PARAMETER, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
					return eContainer.eInverseRemove(this, UML2Package.TEMPLATE_PARAMETER__OWNED_PARAMETERED_ELEMENT, TemplateParameter.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UML2Package.LITERAL_NULL__EANNOTATIONS:
				return getEAnnotations();
			case UML2Package.LITERAL_NULL__OWNED_ELEMENT:
				return getOwnedElements();
			case UML2Package.LITERAL_NULL__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case UML2Package.LITERAL_NULL__OWNED_COMMENT:
				return getOwnedComments();
			case UML2Package.LITERAL_NULL__JDELETED:
				return new Integer(getJ_deleted());
			case UML2Package.LITERAL_NULL__DOCUMENTATION:
				return getDocumentation();
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPES:
				return getAppliedBasicStereotypes();
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPE_VALUES:
				return getAppliedBasicStereotypeValues();
			case UML2Package.LITERAL_NULL__UUID:
				return getUuid();
			case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
				return getTemplateBindings();
			case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
				return getOwnedTemplateSignature();
			case UML2Package.LITERAL_NULL__NAME:
				return getName();
			case UML2Package.LITERAL_NULL__QUALIFIED_NAME:
				return getQualifiedName();
			case UML2Package.LITERAL_NULL__VISIBILITY:
				return getVisibility();
			case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
				return getClientDependencies();
			case UML2Package.LITERAL_NULL__NAME_EXPRESSION:
				return getNameExpression();
			case UML2Package.LITERAL_NULL__OWNED_ANONYMOUS_DEPENDENCIES:
				return getOwnedAnonymousDependencies();
			case UML2Package.LITERAL_NULL__REVERSE_DEPENDENCIES:
				return getReverseDependencies();
			case UML2Package.LITERAL_NULL__REVERSE_GENERALIZATIONS:
				return getReverseGeneralizations();
			case UML2Package.LITERAL_NULL__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
				if (resolve) return getTemplateParameter();
				return basicGetTemplateParameter();
			case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
				return getOwningParameter();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UML2Package.LITERAL_NULL__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__OWNED_COMMENT:
				getOwnedComments().clear();
				getOwnedComments().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__JDELETED:
				setJ_deleted(((Integer)newValue).intValue());
				return;
			case UML2Package.LITERAL_NULL__DOCUMENTATION:
				setDocumentation((String)newValue);
				return;
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPES:
				getAppliedBasicStereotypes().clear();
				getAppliedBasicStereotypes().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPE_VALUES:
				getAppliedBasicStereotypeValues().clear();
				getAppliedBasicStereotypeValues().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__UUID:
				setUuid((String)newValue);
				return;
			case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
				getTemplateBindings().clear();
				getTemplateBindings().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
				setOwnedTemplateSignature((TemplateSignature)newValue);
				return;
			case UML2Package.LITERAL_NULL__NAME:
				setName((String)newValue);
				return;
			case UML2Package.LITERAL_NULL__VISIBILITY:
				setVisibility((VisibilityKind)newValue);
				return;
			case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
				getClientDependencies().clear();
				getClientDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__NAME_EXPRESSION:
				setNameExpression((StringExpression)newValue);
				return;
			case UML2Package.LITERAL_NULL__OWNED_ANONYMOUS_DEPENDENCIES:
				getOwnedAnonymousDependencies().clear();
				getOwnedAnonymousDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__REVERSE_DEPENDENCIES:
				getReverseDependencies().clear();
				getReverseDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__REVERSE_GENERALIZATIONS:
				getReverseGeneralizations().clear();
				getReverseGeneralizations().addAll((Collection)newValue);
				return;
			case UML2Package.LITERAL_NULL__TYPE:
				setType((Type)newValue);
				return;
			case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
				setTemplateParameter((TemplateParameter)newValue);
				return;
			case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
				setOwningParameter((TemplateParameter)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UML2Package.LITERAL_NULL__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case UML2Package.LITERAL_NULL__OWNED_COMMENT:
				getOwnedComments().clear();
				return;
			case UML2Package.LITERAL_NULL__JDELETED:
				setJ_deleted(JDELETED_EDEFAULT);
				return;
			case UML2Package.LITERAL_NULL__DOCUMENTATION:
				setDocumentation(DOCUMENTATION_EDEFAULT);
				return;
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPES:
				getAppliedBasicStereotypes().clear();
				return;
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPE_VALUES:
				getAppliedBasicStereotypeValues().clear();
				return;
			case UML2Package.LITERAL_NULL__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
				getTemplateBindings().clear();
				return;
			case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
				setOwnedTemplateSignature((TemplateSignature)null);
				return;
			case UML2Package.LITERAL_NULL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case UML2Package.LITERAL_NULL__VISIBILITY:
				setVisibility(VISIBILITY_EDEFAULT);
				return;
			case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
				getClientDependencies().clear();
				return;
			case UML2Package.LITERAL_NULL__NAME_EXPRESSION:
				setNameExpression((StringExpression)null);
				return;
			case UML2Package.LITERAL_NULL__OWNED_ANONYMOUS_DEPENDENCIES:
				getOwnedAnonymousDependencies().clear();
				return;
			case UML2Package.LITERAL_NULL__REVERSE_DEPENDENCIES:
				getReverseDependencies().clear();
				return;
			case UML2Package.LITERAL_NULL__REVERSE_GENERALIZATIONS:
				getReverseGeneralizations().clear();
				return;
			case UML2Package.LITERAL_NULL__TYPE:
				setType((Type)null);
				return;
			case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
				setTemplateParameter((TemplateParameter)null);
				return;
			case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
				setOwningParameter((TemplateParameter)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UML2Package.LITERAL_NULL__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case UML2Package.LITERAL_NULL__OWNED_ELEMENT:
				return !getOwnedElements().isEmpty();
			case UML2Package.LITERAL_NULL__OWNER:
				return basicGetOwner() != null;
			case UML2Package.LITERAL_NULL__OWNED_COMMENT:
				return ownedComment != null && !ownedComment.isEmpty();
			case UML2Package.LITERAL_NULL__JDELETED:
				return j_deleted != JDELETED_EDEFAULT;
			case UML2Package.LITERAL_NULL__DOCUMENTATION:
				return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPES:
				return appliedBasicStereotypes != null && !appliedBasicStereotypes.isEmpty();
			case UML2Package.LITERAL_NULL__APPLIED_BASIC_STEREOTYPE_VALUES:
				return appliedBasicStereotypeValues != null && !appliedBasicStereotypeValues.isEmpty();
			case UML2Package.LITERAL_NULL__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case UML2Package.LITERAL_NULL__TEMPLATE_BINDING:
				return templateBinding != null && !templateBinding.isEmpty();
			case UML2Package.LITERAL_NULL__OWNED_TEMPLATE_SIGNATURE:
				return ownedTemplateSignature != null;
			case UML2Package.LITERAL_NULL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case UML2Package.LITERAL_NULL__QUALIFIED_NAME:
				return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
			case UML2Package.LITERAL_NULL__VISIBILITY:
				return visibility != VISIBILITY_EDEFAULT;
			case UML2Package.LITERAL_NULL__CLIENT_DEPENDENCY:
				return clientDependency != null && !clientDependency.isEmpty();
			case UML2Package.LITERAL_NULL__NAME_EXPRESSION:
				return nameExpression != null;
			case UML2Package.LITERAL_NULL__OWNED_ANONYMOUS_DEPENDENCIES:
				return ownedAnonymousDependencies != null && !ownedAnonymousDependencies.isEmpty();
			case UML2Package.LITERAL_NULL__REVERSE_DEPENDENCIES:
				return reverseDependencies != null && !reverseDependencies.isEmpty();
			case UML2Package.LITERAL_NULL__REVERSE_GENERALIZATIONS:
				return reverseGeneralizations != null && !reverseGeneralizations.isEmpty();
			case UML2Package.LITERAL_NULL__TYPE:
				return type != null;
			case UML2Package.LITERAL_NULL__TEMPLATE_PARAMETER:
				return templateParameter != null;
			case UML2Package.LITERAL_NULL__OWNING_PARAMETER:
				return getOwningParameter() != null;
		}
		return eDynamicIsSet(eFeature);
	}


	/**
	 * @see org.eclipse.uml2.ValueSpecification#stringValue()
	 */
	public String stringValue() {
		return String.valueOf((Object) null);
	}

} //LiteralNullImpl
