/*
 * (C) Copyright 2006-20011 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.ecm.platform.reporting.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.platform.reporting.engine.BirtEngine;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.datasource.DataSourceHelper;

import com.ibm.icu.util.ULocale;

/**
 * Helper class to make Birt API easier to use
 *
 * @author Tiry (tdelprat@nuxeo.com)
 */
public class ReportHelper {

    protected static final Log log = LogFactory.getLog(ReportHelper.class);

    public static IReportRunnable getReport(String reportPath) {
        try {
            return BirtEngine.getBirtEngine().openReportDesign(reportPath);
        } catch (BirtException e) {
            throw new NuxeoException(e);
        }
    }

    public static IReportRunnable getReport(InputStream stream) {
        try {
            return BirtEngine.getBirtEngine().openReportDesign(stream);
        } catch (BirtException e) {
            throw new NuxeoException(e);
        }
    }

    public static IReportRunnable getNuxeoReport(InputStream stream) {
        return getNuxeoReport(stream, Framework.getService(RepositoryManager.class).getDefaultRepositoryName());
    }

    public static Map<String, String> getReportMetaData(InputStream stream) {
        IDesignEngine dEngine = BirtEngine.getBirtDesignEngine();
        SessionHandle sh = dEngine.newSessionHandle(ULocale.ENGLISH);
        ReportDesignHandle designHandle;
        try {
            designHandle = sh.openDesign((String) null, stream);
        } catch (BirtException e) {
            throw new NuxeoException(e);
        }

        Map<String, String> meta = new HashMap<String, String>();
        meta.put("title", designHandle.getTitle());
        meta.put("author", designHandle.getAuthor());
        meta.put("description", designHandle.getDescription());
        meta.put("displayName", designHandle.getDisplayName());

        try {
            sh.closeAll(false);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
        return meta;
    }

    public static IReportRunnable getNuxeoReport(InputStream stream, String repositoryName) {
        IDesignEngine dEngine = BirtEngine.getBirtDesignEngine();
        SessionHandle sh = dEngine.newSessionHandle(ULocale.ENGLISH);
        ReportDesignHandle designHandle;
        try {
            designHandle = sh.openDesign((String) null, stream);
        } catch (BirtException e) {
            throw new NuxeoException(e);
        }

        String dsName = DataSourceHelper.getDataSourceRepositoryJNDIName(repositoryName);
        for (Iterator<?> i = designHandle.getDataSources().iterator(); i.hasNext();) {
            OdaDataSourceHandle dsh = (OdaDataSourceHandle) i.next();
            OdaDataSource ds = (OdaDataSource) dsh.getElement();
            ds.setProperty("odaJndiName", DataSourceHelper.getDataSourceJNDIName(dsName));
        }

        IReportRunnable modifiedReport;
        try {
            modifiedReport = BirtEngine.getBirtEngine().openReportDesign(designHandle);
        } catch (BirtException e) {
            throw new NuxeoException(e);
        }
        // Can we really?
        try {
            sh.closeAll(false);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        return modifiedReport;
    }

    public static List<IParameterDefn> getReportParameter(IReportRunnable report) {
        List<IParameterDefn> params = new ArrayList<IParameterDefn>();
        IGetParameterDefinitionTask task = BirtEngine.getBirtEngine().createGetParameterDefinitionTask(report);
        for (Object paramDefn : task.getParameterDefns(false)) {
            if (paramDefn instanceof IParameterDefn) {
                IParameterDefn param = (IParameterDefn) paramDefn;
                params.add(param);
            }
        }
        return params;
    }

}
