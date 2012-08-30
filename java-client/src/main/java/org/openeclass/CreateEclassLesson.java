/*
 * Copyright (C) 2003-2009 Greek Universities Network - GUnet
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

import org.betaconceptframework.betacms.repository.api.model.ContentObject;
import org.betaconceptframework.betacms.repository.api.model.RepositoryUser;
import org.betaconceptframework.betacms.repository.api.model.StringProperty;
import org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials;
import org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient;

/**
 * 
 * @author Thanos Kyritsis
 *
 */
public class CreateEclassLesson {
	
	public static void main(String[] args) throws Exception {

		BetaCmsRepositoryClient cli = new BetaCmsRepositoryClient("localhost");
		BetaCmsCredentials credentials = new BetaCmsCredentials("xxx", "yyy".toCharArray());

		cli.login("myrepo", credentials);
		System.out.println("login OK");
		
		
		ContentObject lesson = cli.getCmsRepositoryEntityFactory().newContentObjectForType("eClassLessonObject", "el");
		
		RepositoryUser owner = cli.getRepositoryUserService().getSystemRepositoryUser();
		lesson.setOwner(owner);
		
		StringProperty canBeReadByProperty = (StringProperty) lesson.getCmsProperty("accessibility.canBeReadBy");
		canBeReadByProperty.addSimpleTypeValue("ALL");
		StringProperty canBeUpdatedByProperty = (StringProperty) lesson.getCmsProperty("accessibility.canBeUpdatedBy");
		canBeUpdatedByProperty.addSimpleTypeValue("NONE");
		StringProperty canBeDeletedByProperty = (StringProperty) lesson.getCmsProperty("accessibility.canBeDeletedBy");
		canBeDeletedByProperty.addSimpleTypeValue("NONE");
		StringProperty canBeTaggedByProperty = (StringProperty) lesson.getCmsProperty("accessibility.canBeTaggedBy");
		canBeTaggedByProperty.addSimpleTypeValue("ALL");
		
		
		StringProperty titleProperty = (StringProperty) lesson.getCmsProperty("profile.title");
		StringProperty lessonDescPR = (StringProperty) lesson.getCmsProperty("lessonDescription");
		StringProperty keywordsPR = (StringProperty) lesson.getCmsProperty("keywords");
		StringProperty copyrightPR = (StringProperty) lesson.getCmsProperty("copyright");
		StringProperty authorsPR = (StringProperty) lesson.getCmsProperty("authors");
		StringProperty projectPR = (StringProperty) lesson.getCmsProperty("project");
		StringProperty commentsPR = (StringProperty) lesson.getCmsProperty("comments");
		//List unitsPR = co.getCmsPropertyList("units");
		//BinaryProperty scosPR = (BinaryProperty) lesson.getCmsProperty("scormFiles");
		
		titleProperty.setSimpleTypeValue("τίτλος");
		lessonDescPR.setSimpleTypeValue("περιγραφή");
		keywordsPR.setSimpleTypeValue("κλειδί1 κλειδί2");
		copyrightPR.setSimpleTypeValue("Πνευματικά Δικαιώματα");
		authorsPR.setSimpleTypeValue("Συγγραφική Ομάδα");
		projectPR.setSimpleTypeValue("Έργο");
		commentsPR.setSimpleTypeValue("Σχόλια");
		
		
		lesson = cli.getContentService().saveContentObject(lesson, false);
		System.out.println("Successfully saved content object");

	}

}
