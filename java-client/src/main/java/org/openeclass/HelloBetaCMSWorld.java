/*
 * Copyright (C) 2005-2009 BetaCONCEPT LP.
 *
 * This file is part of BetaCMS.
 *
 * BetaCMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BetaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BetaCMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openeclass;

import java.util.Calendar;

import org.betaconceptframework.betacms.repository.api.model.CalendarProperty;
import org.betaconceptframework.betacms.repository.api.model.ContentObject;
import org.betaconceptframework.betacms.repository.api.model.RepositoryUser;
import org.betaconceptframework.betacms.repository.api.model.StringProperty;
import org.betaconceptframework.betacms.repository.api.model.query.CacheRegion;
import org.betaconceptframework.betacms.repository.api.model.query.CmsOutcome;
import org.betaconceptframework.betacms.repository.api.model.query.CmsRankedOutcome;
import org.betaconceptframework.betacms.repository.api.model.query.criteria.ContentObjectCriteria;
import org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials;
import org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient;
import org.betaconceptframework.betacms.repository.model.factory.CmsCriteriaFactory;

public class HelloBetaCMSWorld {
	
	public static void main(String[] args) throws Exception {

		BetaCmsRepositoryClient betaCmsRepositoryClient = new BetaCmsRepositoryClient("localhost");
		BetaCmsCredentials credentials = new BetaCmsCredentials("xxx", "yyy".toCharArray());

		//List<CmsRepository> availableRepositories = betaCmsRepositoryClient.getRepositoryService().getAvailableCmsRepositories();
		
		betaCmsRepositoryClient.login("myrepo", credentials);
		System.out.println("login OK");

		
		//Create a new content object instance of type 'genericContentResourceObject' 
		ContentObject helloWorldContentObject = betaCmsRepositoryClient.getCmsRepositoryEntityFactory()
												.newContentObjectForType("genericContentResourceObject", "el");
		
		//Provide owner for content object. Select system repository user
		RepositoryUser owner = betaCmsRepositoryClient.getRepositoryUserService().getSystemRepositoryUser();
		helloWorldContentObject.setOwner(owner);
		
		//Fill content object's properties with values
		
		//Profile properties
		//Title
		StringProperty profileTitleProperty = (StringProperty)helloWorldContentObject.getCmsProperty("profile.title");
		profileTitleProperty.setSimpleTypeValue("Hello βCMS world !");
		
		//Language
		StringProperty profileLanguageProperty = (StringProperty)helloWorldContentObject.getCmsProperty("profile.language");
		profileLanguageProperty.setSimpleTypeValue("el");
		
		//Last Modification Date
		CalendarProperty profileModifiedProperty = (CalendarProperty)helloWorldContentObject.getCmsProperty("profile.modified");
		profileModifiedProperty.setSimpleTypeValue(Calendar.getInstance());

		//Accessibility properties
		StringProperty canBeReadByProperty = (StringProperty)helloWorldContentObject.getCmsProperty("accessibility.canBeReadBy");
		canBeReadByProperty.addSimpleTypeValue("ALL");
		
		StringProperty canBeUpdatedByProperty = (StringProperty)helloWorldContentObject.getCmsProperty("accessibility.canBeUpdatedBy");
		canBeUpdatedByProperty.addSimpleTypeValue("NONE");
		
		StringProperty canBeDeletedByProperty = (StringProperty)helloWorldContentObject.getCmsProperty("accessibility.canBeDeletedBy");
		canBeDeletedByProperty.addSimpleTypeValue("NONE");
		
		StringProperty canBeTaggedByProperty = (StringProperty)helloWorldContentObject.getCmsProperty("accessibility.canBeTaggedBy");
		canBeTaggedByProperty.addSimpleTypeValue("ALL");
		

		// body
		String body = "<b>My first βCMS content object</b>";
		StringProperty bodyProperty = (StringProperty)helloWorldContentObject.getCmsProperty("body");
		bodyProperty.setSimpleTypeValue(body);
		
		// save content object
		helloWorldContentObject = betaCmsRepositoryClient.getContentService().saveContentObject(helloWorldContentObject, false);
		System.out.println("Successfully saved content object");
		
		// retrieve content object
		helloWorldContentObject = betaCmsRepositoryClient.getContentService().getContentObjectById(helloWorldContentObject.getId(), CacheRegion.FIVE_MINUTES);
		System.out.println("Successfully retrieved content object");
		System.out.println(helloWorldContentObject.toXml());
		
		//betaCmsRepositoryClient.getContentService().deleteContentObject(helloWorldContentObject.getId());
		//System.out.println("Successfully deleted content object");
		
		//owner.getSpace().addContentObjectReference(helloWorldContentObject.getId());
		//System.out.println("Successfully added content object to owner's space");
		//betaCmsRepositoryClient.getSpaceService().getOrganizationSpace().addContentObjectReference(helloWorldContentObject.getId());
		//System.out.println("Successfully added content object to organization space");
		
		
		
		//Search content object
		//Create search criteria
		ContentObjectCriteria contentObjectCriteria = CmsCriteriaFactory.newContentObjectCriteria();
		contentObjectCriteria.addFullTextSearchCriterion("Hello βCMS world");
		
		//You can cache results
		contentObjectCriteria.setCacheable(CacheRegion.FIVE_MINUTES);
		
		//Execute query
		CmsOutcome<CmsRankedOutcome<ContentObject>> cmsOutcome = betaCmsRepositoryClient.getContentService().searchContentObjects(contentObjectCriteria);
		
		//Iterate through results
		for (CmsRankedOutcome<ContentObject> cmsRankedOutcome : cmsOutcome.getResults()) {
			ContentObject contentObject = cmsRankedOutcome.getCmsRepositoryEntity();
			
			StringProperty titleProperty = (StringProperty)contentObject.getCmsProperty("profile.title");
			String contentObjectType = contentObject.getContentObjectType();
			
			System.out.println("Found content object of type "+contentObjectType+" with title "+titleProperty.getSimpleTypeValue());
			
			// delete every entry
			//betaCmsRepositoryClient.getContentService().deleteContentObject(contentObject.getId());
		}
	}

}
