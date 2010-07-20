/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistributionImpl {
    private Log log = null;

    public EntityAdminDistributionImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(RespondingGatewaySendAlertMessageType message, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        logEntityAdminDist(message, assertion);


        CMUrlInfos urlInfoList = getEndpoints(target);

        if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
            log.warn("No targets were found for the Patient Discovery Request");

        }

            for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                //create a new request to send out to each target community
               
                //check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = checkPolicy(message, assertion, urlInfo.getHcid());

                if (bIsPolicyOk) {
                    sendToNhinProxy(message, assertion, urlInfo.getUrl());

                } //if (bIsPolicyOk)
                else {
                    log.error("The policy engine evaluated the request and denied the request.");
                } //else policy enging did not return a permit response
            }

        
    }
    private void logEntityAdminDist(RespondingGatewaySendAlertMessageType request, AssertionType assertion) {
        // Audit the XDR Request Message sent on the Nhin Interface
        AcknowledgementType ack = new AdminDistributionAuditLogger().auditEntityAdminDist(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }
    protected boolean checkPolicy(RespondingGatewaySendAlertMessageType request, AssertionType assertion, String hcid) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return new AdminDistributionPolicyChecker().checkOutgoingPolicy(request, hcid);
        
    }
    protected void sendToNhinProxy(RespondingGatewaySendAlertMessageType newRequest, AssertionType assertion, String url) {
    }

}