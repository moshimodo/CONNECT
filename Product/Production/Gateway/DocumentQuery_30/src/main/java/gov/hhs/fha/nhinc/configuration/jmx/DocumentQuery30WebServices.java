/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.configuration.jmx;

import gov.hhs.fha.nhinc.docquery._30.entity.EntityDocQuerySecured;
import gov.hhs.fha.nhinc.docquery._30.entity.EntityDocQueryUnsecured;
import gov.hhs.fha.nhinc.docquery._30.nhin.DocQuery;
import gov.hhs.fha.nhinc.docquery.inbound.InboundDocQuery;
import gov.hhs.fha.nhinc.docquery.outbound.OutboundDocQuery;

import javax.servlet.ServletContext;

/**
 * The Class DocumentQuery30WebServices.
 *
 * @author msw
 */
public class DocumentQuery30WebServices extends AbstractWebServicesMXBean implements WebServicesMXBean {

    /** The Constant NHIN_DQ_BEAN_NAME. */
    private static final String NHIN_DQ_BEAN_NAME = "nhinDQ";
    
    /** The Constant ENTITY_UNSECURED_DQ_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_DQ_BEAN_NAME = "entityDQUnsecured";
    
    /** The Constant ENTITY_SECURED_DQ_BEAN_NAME. */
    private static final String ENTITY_SECURED_DQ_BEAN_NAME = "entityDQSecured";

    /** The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docquery.inbound.StandardInboundDocQuery";
    
    /** The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docquery.inbound.PassthroughInboundDocQuery";
    
    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docquery.outbound.StandardOutboundDocQuery";
    
    /** The Constant DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docquery.outbound.PassthroughOutboundDocQuery";

    /**
     * Instantiates a new document query30 web services.
     *
     * @param sc the sc
     */
    public DocumentQuery30WebServices(ServletContext sc) {
        super(sc);
    }


    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_DQ_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_DQ_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_DQ_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureInboundImplementation(className);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundStd()
     */
    @Override
    public void configureInboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        configureInboundImplementation(DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundPassthru()
     */
    @Override
    public void configureInboundPassthru() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureInboundImplementation(DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureOutboundImplementation(className);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureOutboundStd()
     */
    @Override
    public void configureOutboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        configureOutboundImplementation(DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureOutboundPassthru()
     */
    @Override
    public void configureOutboundPassthru() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        configureOutboundImplementation(DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImplementation(java.lang.String)
     */
    protected void configureInboundImplementation(String className) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        DocQuery docQuery = null;
        InboundDocQuery inboundDocQuery = null;

        docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
        inboundDocQuery = retrieveDependency(InboundDocQuery.class, className);
        docQuery.setInboundDocQuery(inboundDocQuery);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImplementation(java.lang.String)
     */
    protected void configureOutboundImplementation(String className) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        EntityDocQueryUnsecured entityUnsecuredDocQuery = null;
        EntityDocQuerySecured entitySecuredDocQuery = null;
        OutboundDocQuery outboundDocQuery = null;

        entityUnsecuredDocQuery = retrieveBean(EntityDocQueryUnsecured.class, getEntityUnsecuredBeanName());
        entitySecuredDocQuery = retrieveBean(EntityDocQuerySecured.class, getEntitySecuredBeanName());
        outboundDocQuery = retrieveDependency(OutboundDocQuery.class, className);

        entityUnsecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
        entitySecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        DocQuery docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
        InboundDocQuery inboundDocQuery = docQuery.getInboundDocQuery();
        if (DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(inboundDocQuery.getClass().getName())) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public boolean isOutboundPassthru() {
        boolean isPassthru = false;
        EntityDocQueryUnsecured entityDocQuery = retrieveBean(EntityDocQueryUnsecured.class,
                getEntityUnsecuredBeanName());
        OutboundDocQuery outboundDocQuery = entityDocQuery.getOutboundDocQuery();
        if (DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(outboundDocQuery.getClass().getName())) {
            isPassthru = true;
        }
        return isPassthru;
    }

}
