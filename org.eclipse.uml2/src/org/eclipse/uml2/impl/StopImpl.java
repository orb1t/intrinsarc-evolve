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
 * $Id: StopImpl.java,v 1.1 2009-03-04 23:06:43 andrew Exp $
 */
package org.eclipse.uml2.impl;

import java.util.Collection;

import java.util.Map;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.Interaction;
import org.eclipse.uml2.InteractionOperand;
import org.eclipse.uml2.Message;
import org.eclipse.uml2.Stop;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.UML2Package;
import org.eclipse.uml2.VisibilityKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stop</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class StopImpl extends EventOccurrenceImpl implements Stop {
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
	protected StopImpl() {
		super();
		
		if (eAdapters().size() == 0)
			eAdapters().add(com.hopstepjump.notifications.GlobalNotifier.getSingleton());
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UML2Package.eINSTANCE.getStop();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case UML2Package.STOP__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__TEMPLATE_BINDING:
					return ((InternalEList)getTemplateBindings()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
					if (ownedTemplateSignature != null)
						msgs = ((InternalEObject)ownedTemplateSignature).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE, null, msgs);
					return basicSetOwnedTemplateSignature((TemplateSignature)otherEnd, msgs);
				case UML2Package.STOP__CLIENT_DEPENDENCY:
					return ((InternalEList)getClientDependencies()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__COVERED:
					return ((InternalEList)getCovereds()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__ENCLOSING_INTERACTION:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, UML2Package.STOP__ENCLOSING_INTERACTION, msgs);
				case UML2Package.STOP__ENCLOSING_OPERAND:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, UML2Package.STOP__ENCLOSING_OPERAND, msgs);
				case UML2Package.STOP__RECEIVE_MESSAGE:
					if (receiveMessage != null)
						msgs = ((InternalEObject)receiveMessage).eInverseRemove(this, UML2Package.MESSAGE__RECEIVE_EVENT, Message.class, msgs);
					return basicSetReceiveMessage((Message)otherEnd, msgs);
				case UML2Package.STOP__SEND_MESSAGE:
					if (sendMessage != null)
						msgs = ((InternalEObject)sendMessage).eInverseRemove(this, UML2Package.MESSAGE__SEND_EVENT, Message.class, msgs);
					return basicSetSendMessage((Message)otherEnd, msgs);
				case UML2Package.STOP__START_EXEC:
					return ((InternalEList)getStartExecs()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__FINISH_EXEC:
					return ((InternalEList)getFinishExecs()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__TO_AFTER:
					return ((InternalEList)getToAfters()).basicAdd(otherEnd, msgs);
				case UML2Package.STOP__TO_BEFORE:
					return ((InternalEList)getToBefores()).basicAdd(otherEnd, msgs);
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
				case UML2Package.STOP__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__OWNED_COMMENT:
					return ((InternalEList)getOwnedComments()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__APPLIED_BASIC_STEREOTYPE_VALUES:
					return ((InternalEList)getAppliedBasicStereotypeValues()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__TEMPLATE_BINDING:
					return ((InternalEList)getTemplateBindings()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
					return basicSetOwnedTemplateSignature(null, msgs);
				case UML2Package.STOP__CLIENT_DEPENDENCY:
					return ((InternalEList)getClientDependencies()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__NAME_EXPRESSION:
					return basicSetNameExpression(null, msgs);
				case UML2Package.STOP__OWNED_ANONYMOUS_DEPENDENCIES:
					return ((InternalEList)getOwnedAnonymousDependencies()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__COVERED:
					return ((InternalEList)getCovereds()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__GENERAL_ORDERING:
					return ((InternalEList)getGeneralOrderings()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__ENCLOSING_INTERACTION:
					return eBasicSetContainer(null, UML2Package.STOP__ENCLOSING_INTERACTION, msgs);
				case UML2Package.STOP__ENCLOSING_OPERAND:
					return eBasicSetContainer(null, UML2Package.STOP__ENCLOSING_OPERAND, msgs);
				case UML2Package.STOP__RECEIVE_MESSAGE:
					return basicSetReceiveMessage(null, msgs);
				case UML2Package.STOP__SEND_MESSAGE:
					return basicSetSendMessage(null, msgs);
				case UML2Package.STOP__START_EXEC:
					return ((InternalEList)getStartExecs()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__FINISH_EXEC:
					return ((InternalEList)getFinishExecs()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__TO_AFTER:
					return ((InternalEList)getToAfters()).basicRemove(otherEnd, msgs);
				case UML2Package.STOP__TO_BEFORE:
					return ((InternalEList)getToBefores()).basicRemove(otherEnd, msgs);
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
				case UML2Package.STOP__ENCLOSING_INTERACTION:
					return eContainer.eInverseRemove(this, UML2Package.INTERACTION__FRAGMENT, Interaction.class, msgs);
				case UML2Package.STOP__ENCLOSING_OPERAND:
					return eContainer.eInverseRemove(this, UML2Package.INTERACTION_OPERAND__FRAGMENT, InteractionOperand.class, msgs);
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
			case UML2Package.STOP__EANNOTATIONS:
				return getEAnnotations();
			case UML2Package.STOP__OWNED_ELEMENT:
				return getOwnedElements();
			case UML2Package.STOP__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case UML2Package.STOP__OWNED_COMMENT:
				return getOwnedComments();
			case UML2Package.STOP__JDELETED:
				return new Integer(getJ_deleted());
			case UML2Package.STOP__DOCUMENTATION:
				return getDocumentation();
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPES:
				return getAppliedBasicStereotypes();
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPE_VALUES:
				return getAppliedBasicStereotypeValues();
			case UML2Package.STOP__UUID:
				return getUuid();
			case UML2Package.STOP__TEMPLATE_BINDING:
				return getTemplateBindings();
			case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
				return getOwnedTemplateSignature();
			case UML2Package.STOP__NAME:
				return getName();
			case UML2Package.STOP__QUALIFIED_NAME:
				return getQualifiedName();
			case UML2Package.STOP__VISIBILITY:
				return getVisibility();
			case UML2Package.STOP__CLIENT_DEPENDENCY:
				return getClientDependencies();
			case UML2Package.STOP__NAME_EXPRESSION:
				return getNameExpression();
			case UML2Package.STOP__OWNED_ANONYMOUS_DEPENDENCIES:
				return getOwnedAnonymousDependencies();
			case UML2Package.STOP__REVERSE_DEPENDENCIES:
				return getReverseDependencies();
			case UML2Package.STOP__REVERSE_GENERALIZATIONS:
				return getReverseGeneralizations();
			case UML2Package.STOP__COVERED:
				return getCovereds();
			case UML2Package.STOP__GENERAL_ORDERING:
				return getGeneralOrderings();
			case UML2Package.STOP__ENCLOSING_INTERACTION:
				return getEnclosingInteraction();
			case UML2Package.STOP__ENCLOSING_OPERAND:
				return getEnclosingOperand();
			case UML2Package.STOP__RECEIVE_MESSAGE:
				if (resolve) return getReceiveMessage();
				return basicGetReceiveMessage();
			case UML2Package.STOP__SEND_MESSAGE:
				if (resolve) return getSendMessage();
				return basicGetSendMessage();
			case UML2Package.STOP__START_EXEC:
				return getStartExecs();
			case UML2Package.STOP__FINISH_EXEC:
				return getFinishExecs();
			case UML2Package.STOP__TO_AFTER:
				return getToAfters();
			case UML2Package.STOP__TO_BEFORE:
				return getToBefores();
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
			case UML2Package.STOP__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__OWNED_COMMENT:
				getOwnedComments().clear();
				getOwnedComments().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__JDELETED:
				setJ_deleted(((Integer)newValue).intValue());
				return;
			case UML2Package.STOP__DOCUMENTATION:
				setDocumentation((String)newValue);
				return;
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPES:
				getAppliedBasicStereotypes().clear();
				getAppliedBasicStereotypes().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPE_VALUES:
				getAppliedBasicStereotypeValues().clear();
				getAppliedBasicStereotypeValues().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__UUID:
				setUuid((String)newValue);
				return;
			case UML2Package.STOP__TEMPLATE_BINDING:
				getTemplateBindings().clear();
				getTemplateBindings().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
				setOwnedTemplateSignature((TemplateSignature)newValue);
				return;
			case UML2Package.STOP__NAME:
				setName((String)newValue);
				return;
			case UML2Package.STOP__VISIBILITY:
				setVisibility((VisibilityKind)newValue);
				return;
			case UML2Package.STOP__CLIENT_DEPENDENCY:
				getClientDependencies().clear();
				getClientDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__NAME_EXPRESSION:
				setNameExpression((StringExpression)newValue);
				return;
			case UML2Package.STOP__OWNED_ANONYMOUS_DEPENDENCIES:
				getOwnedAnonymousDependencies().clear();
				getOwnedAnonymousDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__REVERSE_DEPENDENCIES:
				getReverseDependencies().clear();
				getReverseDependencies().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__REVERSE_GENERALIZATIONS:
				getReverseGeneralizations().clear();
				getReverseGeneralizations().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__COVERED:
				getCovereds().clear();
				getCovereds().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__GENERAL_ORDERING:
				getGeneralOrderings().clear();
				getGeneralOrderings().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__ENCLOSING_INTERACTION:
				setEnclosingInteraction((Interaction)newValue);
				return;
			case UML2Package.STOP__ENCLOSING_OPERAND:
				setEnclosingOperand((InteractionOperand)newValue);
				return;
			case UML2Package.STOP__RECEIVE_MESSAGE:
				setReceiveMessage((Message)newValue);
				return;
			case UML2Package.STOP__SEND_MESSAGE:
				setSendMessage((Message)newValue);
				return;
			case UML2Package.STOP__START_EXEC:
				getStartExecs().clear();
				getStartExecs().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__FINISH_EXEC:
				getFinishExecs().clear();
				getFinishExecs().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__TO_AFTER:
				getToAfters().clear();
				getToAfters().addAll((Collection)newValue);
				return;
			case UML2Package.STOP__TO_BEFORE:
				getToBefores().clear();
				getToBefores().addAll((Collection)newValue);
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
			case UML2Package.STOP__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case UML2Package.STOP__OWNED_COMMENT:
				getOwnedComments().clear();
				return;
			case UML2Package.STOP__JDELETED:
				setJ_deleted(JDELETED_EDEFAULT);
				return;
			case UML2Package.STOP__DOCUMENTATION:
				setDocumentation(DOCUMENTATION_EDEFAULT);
				return;
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPES:
				getAppliedBasicStereotypes().clear();
				return;
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPE_VALUES:
				getAppliedBasicStereotypeValues().clear();
				return;
			case UML2Package.STOP__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case UML2Package.STOP__TEMPLATE_BINDING:
				getTemplateBindings().clear();
				return;
			case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
				setOwnedTemplateSignature((TemplateSignature)null);
				return;
			case UML2Package.STOP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case UML2Package.STOP__VISIBILITY:
				setVisibility(VISIBILITY_EDEFAULT);
				return;
			case UML2Package.STOP__CLIENT_DEPENDENCY:
				getClientDependencies().clear();
				return;
			case UML2Package.STOP__NAME_EXPRESSION:
				setNameExpression((StringExpression)null);
				return;
			case UML2Package.STOP__OWNED_ANONYMOUS_DEPENDENCIES:
				getOwnedAnonymousDependencies().clear();
				return;
			case UML2Package.STOP__REVERSE_DEPENDENCIES:
				getReverseDependencies().clear();
				return;
			case UML2Package.STOP__REVERSE_GENERALIZATIONS:
				getReverseGeneralizations().clear();
				return;
			case UML2Package.STOP__COVERED:
				getCovereds().clear();
				return;
			case UML2Package.STOP__GENERAL_ORDERING:
				getGeneralOrderings().clear();
				return;
			case UML2Package.STOP__ENCLOSING_INTERACTION:
				setEnclosingInteraction((Interaction)null);
				return;
			case UML2Package.STOP__ENCLOSING_OPERAND:
				setEnclosingOperand((InteractionOperand)null);
				return;
			case UML2Package.STOP__RECEIVE_MESSAGE:
				setReceiveMessage((Message)null);
				return;
			case UML2Package.STOP__SEND_MESSAGE:
				setSendMessage((Message)null);
				return;
			case UML2Package.STOP__START_EXEC:
				getStartExecs().clear();
				return;
			case UML2Package.STOP__FINISH_EXEC:
				getFinishExecs().clear();
				return;
			case UML2Package.STOP__TO_AFTER:
				getToAfters().clear();
				return;
			case UML2Package.STOP__TO_BEFORE:
				getToBefores().clear();
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
			case UML2Package.STOP__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case UML2Package.STOP__OWNED_ELEMENT:
				return !getOwnedElements().isEmpty();
			case UML2Package.STOP__OWNER:
				return basicGetOwner() != null;
			case UML2Package.STOP__OWNED_COMMENT:
				return ownedComment != null && !ownedComment.isEmpty();
			case UML2Package.STOP__JDELETED:
				return j_deleted != JDELETED_EDEFAULT;
			case UML2Package.STOP__DOCUMENTATION:
				return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPES:
				return appliedBasicStereotypes != null && !appliedBasicStereotypes.isEmpty();
			case UML2Package.STOP__APPLIED_BASIC_STEREOTYPE_VALUES:
				return appliedBasicStereotypeValues != null && !appliedBasicStereotypeValues.isEmpty();
			case UML2Package.STOP__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case UML2Package.STOP__TEMPLATE_BINDING:
				return templateBinding != null && !templateBinding.isEmpty();
			case UML2Package.STOP__OWNED_TEMPLATE_SIGNATURE:
				return ownedTemplateSignature != null;
			case UML2Package.STOP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case UML2Package.STOP__QUALIFIED_NAME:
				return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
			case UML2Package.STOP__VISIBILITY:
				return visibility != VISIBILITY_EDEFAULT;
			case UML2Package.STOP__CLIENT_DEPENDENCY:
				return clientDependency != null && !clientDependency.isEmpty();
			case UML2Package.STOP__NAME_EXPRESSION:
				return nameExpression != null;
			case UML2Package.STOP__OWNED_ANONYMOUS_DEPENDENCIES:
				return ownedAnonymousDependencies != null && !ownedAnonymousDependencies.isEmpty();
			case UML2Package.STOP__REVERSE_DEPENDENCIES:
				return reverseDependencies != null && !reverseDependencies.isEmpty();
			case UML2Package.STOP__REVERSE_GENERALIZATIONS:
				return reverseGeneralizations != null && !reverseGeneralizations.isEmpty();
			case UML2Package.STOP__COVERED:
				return !getCovereds().isEmpty();
			case UML2Package.STOP__GENERAL_ORDERING:
				return generalOrdering != null && !generalOrdering.isEmpty();
			case UML2Package.STOP__ENCLOSING_INTERACTION:
				return getEnclosingInteraction() != null;
			case UML2Package.STOP__ENCLOSING_OPERAND:
				return getEnclosingOperand() != null;
			case UML2Package.STOP__RECEIVE_MESSAGE:
				return receiveMessage != null;
			case UML2Package.STOP__SEND_MESSAGE:
				return sendMessage != null;
			case UML2Package.STOP__START_EXEC:
				return startExec != null && !startExec.isEmpty();
			case UML2Package.STOP__FINISH_EXEC:
				return finishExec != null && !finishExec.isEmpty();
			case UML2Package.STOP__TO_AFTER:
				return toAfter != null && !toAfter.isEmpty();
			case UML2Package.STOP__TO_BEFORE:
				return toBefore != null && !toBefore.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}


} //StopImpl