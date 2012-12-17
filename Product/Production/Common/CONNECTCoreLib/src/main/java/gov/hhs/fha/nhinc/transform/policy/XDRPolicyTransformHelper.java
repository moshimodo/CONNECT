/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import org.apache.log4j.Logger;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;

/**
 * 
 * @author dunnek
 */
public class XDRPolicyTransformHelper {

    private Logger log = Logger.getLogger(XDRPolicyTransformHelper.class);
    private static final String ActionInValue = "XDRIn";
    private static final String ActionOutValue = "XDROut";
    private static final String XDRRESPONSE_ACTION_IN_VALUE = "XDRResponseIn";
    private static final String XDRRESPONSE_ACTION_OUT_VALUE = "XDRResponseOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    /**
     * Transform method to create a CheckPolicyRequest object from a 201306 message
     * 
     * @param request
     * @return CheckPolicyRequestType
     */

    public CheckPolicyRequestType transformXDRToCheckPolicy(XDREventType event) {
        log.debug("Begin -- XDRPolicyTransformHelper.transformXDRToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            log.debug("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage()
                .getAssertion());
        log.debug("transformXDRToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        String encodedPatientId = getPatientIdFromEvent(event);
        String assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(encodedPatientId);
        String patId = PatientIdFormatUtil.parsePatientId(encodedPatientId);

        if (patId != null && assigningAuthorityId != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(
                    attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString,
                            assigningAuthorityId));

            log.debug("transformXDRToCheckPolicy: sStrippedPatientId = " + patId);
            resource.getAttribute().add(
                    attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, patId));

            request.getResource().add(resource);
        }

        log.debug("transformXDRToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        if (NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        } else if (NhincConstants.POLICYENGINE_INBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getMessage().getAssertion());
        log.debug("End -- XDRPolicyTransformHelper.transformXDRToCheckPolicy()");
        return checkPolicyRequest;
    }

    private String getPatientIdFromEvent(XDREventType event) {
        return getIdentifiersFromRequest(event.getMessage().getProvideAndRegisterDocumentSetRequest());
    }

    private String getIdentifiersFromRequest(ProvideAndRegisterDocumentSetRequestType request) {
        String result = "";

        if (request == null) {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType was null"));
            return null;
        }

        if (request.getSubmitObjectsRequest() == null)

        {
            log.error(("Incoming ProvideAndRegisterDocumentSetRequestType metadata was null"));
            return null;
        }

        System.out.println(request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable());
        RegistryObjectListType object = request.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            System.out.println(object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class)) {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                System.out.println(registryPackage.getSlot().size());

                for (int y = 0; y < registryPackage.getExternalIdentifier().size(); y++) {
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0)
                            .getValue();
                    if (test.equals("XDSSubmissionSet.patientId")) {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }

                }

            }
        }

        return result;
    }

    /**
     * Transform method to create a CheckPolicyRequest object
     * 
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformXDREntityToCheckPolicy(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 
     * @param event
     * @return
     */
    public CheckPolicyRequestType transformXDRResponseToCheckPolicy(XDRResponseEventType event) {

        log.debug("Begin -- XDRPolicyTransformHelper.transformXDRResponseToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            log.debug("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage()
                .getAssertion());
        log.debug("transformXDRResponseToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        log.debug("transformXDRResponseToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        if (NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(XDRRESPONSE_ACTION_OUT_VALUE));
        } else if (NhincConstants.POLICYENGINE_INBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(XDRRESPONSE_ACTION_IN_VALUE));
        }

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getMessage().getAssertion());
        log.debug("End -- XDRPolicyTransformHelper.transformXDRResponseToCheckPolicy()");
        return checkPolicyRequest;
    }

}
