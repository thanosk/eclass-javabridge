/*
 * Copyright (C) 2003-2010 Greek Universities Network - GUnet
 *
 * This file is part of Open eClass
 *
 * Open eClass is an open platform distributed in the hope that it will
 * be useful (without any warranty), under the terms of the GNU (General
 * Public License) as published by the Free Software Foundation.
 *
 * Open eClass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open eClass.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openeclass;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.betaconceptframework.betacms.repository.api.model.BinaryChannel;
import org.betaconceptframework.betacms.repository.api.model.BinaryProperty;
import org.betaconceptframework.betacms.repository.api.model.ComplexCmsProperty;
import org.betaconceptframework.betacms.repository.api.model.ContentObject;
import org.betaconceptframework.betacms.repository.api.model.ContentObjectProperty;
import org.betaconceptframework.betacms.repository.api.model.StringProperty;
import org.betaconceptframework.betacms.repository.api.model.query.CacheRegion;
import org.betaconceptframework.betacms.repository.api.model.query.CmsOutcome;
import org.betaconceptframework.betacms.repository.api.model.query.CmsRankedOutcome;
import org.betaconceptframework.betacms.repository.api.model.query.criteria.ContentObjectCriteria;
import org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials;
import org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient;
import org.betaconceptframework.betacms.repository.model.factory.CmsCriteriaFactory;

/**
 * 
 * @author Thanos Kyritsis
 *
 */
public class HelloEclassWorld {
	
	public static void main(String[] args) throws Exception {

		BetaCmsRepositoryClient cli = new BetaCmsRepositoryClient("localhost");
		BetaCmsCredentials credentials = new BetaCmsCredentials("xxx", "yyy".toCharArray());

		cli.login("myrepo", credentials);
		System.out.println("login OK");
		
		ContentObjectCriteria criteria = CmsCriteriaFactory.newContentObjectCriteria();
		//criteria.addFullTextSearchCriterion("title");
		criteria.addContentObjectTypeEqualsCriterion("eClassLessonObject");
		criteria.setCacheable(CacheRegion.FIVE_MINUTES);
		
		//Execute query
		CmsOutcome<CmsRankedOutcome<ContentObject>> cmsOutcome = cli.getContentService().searchContentObjects(criteria);
		
		//Iterate through results
		for (CmsRankedOutcome<ContentObject> cmsRankedOutcome : cmsOutcome.getResults()) {
			ContentObject co = cmsRankedOutcome.getCmsRepositoryEntity();
//			ContentObject co = cli.getContentService().getContentObjectById("fa146c04-6511-4f08-85f5-a864c3671c5b", CacheRegion.FIVE_MINUTES);
			String contentObjectType = co.getContentObjectType();
			
			StringProperty titleProperty = (StringProperty) co.getCmsProperty("profile.title");
			StringProperty lessonDescPR = (StringProperty) co.getCmsProperty("lessonDescription");
			StringProperty keywordsPR = (StringProperty) co.getCmsProperty("keywords");
			StringProperty copyrightPR = (StringProperty) co.getCmsProperty("copyright");
			StringProperty authorsPR = (StringProperty) co.getCmsProperty("authors");
			StringProperty projectPR = (StringProperty) co.getCmsProperty("project");
			StringProperty commentsPR = (StringProperty) co.getCmsProperty("comments");
			ContentObjectProperty scosPR = (ContentObjectProperty) co.getCmsProperty("scormFiles");
			ContentObjectProperty docsPR = (ContentObjectProperty) co.getCmsProperty("documentFiles");
			List unitsPR = co.getCmsPropertyList("units");
			
			System.out.println("Found content object of type " + contentObjectType);
			System.out.println("Title: " + titleProperty.getSimpleTypeValue());
			System.out.println("lessonDescription: " + lessonDescPR.getSimpleTypeValue());
			System.out.println("keywords: " + keywordsPR.getSimpleTypeValue());
			System.out.println("copyright: " + copyrightPR.getSimpleTypeValue());
			System.out.println("author: " + authorsPR.getSimpleTypeValue());
			System.out.println("project: " + projectPR.getSimpleTypeValue());
			System.out.println("comments: " + commentsPR.getSimpleTypeValue());
			
			if (unitsPR != null && unitsPR.size() > 0) {
				System.out.println("=== Units("+unitsPR.size()+") ===");
				for (int i = 0; i < unitsPR.size(); i++) {
					ComplexCmsProperty ccprop = (ComplexCmsProperty) unitsPR.get(i);
					System.out.println("title["+i+"]: " + ((StringProperty) ccprop.getChildProperty("title")).getSimpleTypeValue() );
					System.out.println("description["+i+"]: " + ((StringProperty) ccprop.getChildProperty("description")).getSimpleTypeValue() );

					// Scorms
					ContentObjectProperty unitscosPR = (ContentObjectProperty) ccprop.getChildProperty("scormFiles");
					if (unitscosPR != null && unitscosPR.getSimpleTypeValues().size() > 0) {
						List<ContentObject> unitscos = unitscosPR.getSimpleTypeValues();
						System.out.println("   === SCOs("+ unitscos.size() +") ===");
						for (ContentObject unitsco : unitscos) {
							BinaryProperty unitbp = (BinaryProperty) unitsco.getCmsProperty("content");
							BinaryChannel unitbc = unitbp.getSimpleTypeValue();
							System.out.println(unitbc.getSourceFilename() +" "+ unitbc.getMimeType() +" "+ unitbc.getCalculatedSize() +" || "+ unitbc.getDirectURL());
						}
					}
					
					// Documents
					ContentObjectProperty unitdocsPR = (ContentObjectProperty) ccprop.getChildProperty("documentFiles");
					if (unitdocsPR != null && unitdocsPR.getSimpleTypeValues().size() > 0) {
						List<ContentObject> unitdocs = unitdocsPR.getSimpleTypeValues();
						System.out.println("   === Documents("+ unitdocs.size() +") ===");
						for (ContentObject unitdoc : unitdocs) {
							BinaryProperty unitdocbp = (BinaryProperty) unitdoc.getCmsProperty("content");
							BinaryChannel unitdocbc = unitdocbp.getSimpleTypeValue();
							System.out.println(unitdocbc.getSourceFilename() +" "+ unitdocbc.getMimeType() +" "+ unitdocbc.getCalculatedSize() +" ||  "+ unitdocbc.getDirectURL());
						}
					}
					
					// Texts
					StringProperty textsPR = (StringProperty) ccprop.getChildProperty("texts");
					List<String> texts = textsPR.getSimpleTypeValues();
					if (texts != null && texts.size() > 0) {
						System.out.println("   === Texts("+ texts.size() +") ===");
						for (int j = 0; j < texts.size(); j++) {
							System.out.println("      text["+j+"]: " + texts.get(j));
						}
					}
					
				}
			}
			
			if (scosPR != null && scosPR.getSimpleTypeValues().size() > 0) {
				List<ContentObject> scos = scosPR.getSimpleTypeValues();
				System.out.println("=== SCOs("+ scos.size() +") ===");
				for (ContentObject sco : scos) {
					BinaryProperty bp = (BinaryProperty) sco.getCmsProperty("content");
					BinaryChannel bc = bp.getSimpleTypeValue();
					System.out.println(bc.getSourceFilename() +" "+ bc.getMimeType() +" "+  bc.getCalculatedSize() +" || "+ bc.getDirectURL());
					//System.out.println("url: " + bc.getDirectURL());
					/*File f = new File("/tmp/"+bc.getSourceFilename());
					FileOutputStream fos = new FileOutputStream(f);
					DataOutputStream dos = new DataOutputStream(fos);
					dos.write(bc.getContent());
					dos.close();
					fos.close();*/
				}
			}
			
			if (docsPR != null && docsPR.getSimpleTypeValues().size() > 0) {
				List<ContentObject> docs = docsPR.getSimpleTypeValues();
				System.out.println("=== Documents("+ docs.size() +") ===");
				for (ContentObject doc : docs) {
					BinaryProperty docbp = (BinaryProperty) doc.getCmsProperty("content");
					BinaryChannel docbc = docbp.getSimpleTypeValue();
					System.out.println(docbc.getSourceFilename() +" "+ docbc.getMimeType() +" "+ docbc.getCalculatedSize() +" || "+ docbc.getDirectURL());
				}
			}
			
			//System.out.println(co.toXml());
			
		}
	}

}
