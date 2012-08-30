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

import org.betaconceptframework.betacms.repository.api.model.RepositoryUser;
import org.betaconceptframework.betacms.repository.api.model.Taxonomy;
import org.betaconceptframework.betacms.repository.api.model.Topic;
import org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials;
import org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient;

/**
 * 
 * @author Thanos Kyritsis
 *
 */
public class HelloFileWorld {
	
	public static void main(String[] args) throws Exception {

		BetaCmsRepositoryClient cli = new BetaCmsRepositoryClient("localhost");
		BetaCmsCredentials credentials = new BetaCmsCredentials("xxx", "yyy".toCharArray());

		cli.login("myrepo", credentials);
		System.out.println("login OK");
		
		RepositoryUser systemUser = cli.getRepositoryUserService().getSystemRepositoryUser();
		
		Taxonomy cdTax = cli.getTaxonomyService().getTaxonomy("contentDesignation", "el");
		if (cdTax == null) {
			System.out.println("contentDesignation taxonomy is null, creating ...");
			cdTax = cli.getCmsRepositoryEntityFactory().newTaxonomy();
			cdTax.setName("contentDesignation");
			cdTax.addLocalizedLabel("en", "contentDesignation");
			cdTax.addLocalizedLabel("el", "contentDesignation");        
			cdTax = cli.getTaxonomyService().saveTaxonomy(cdTax);
			System.out.println("contentDesignation Taxonomy created successfully!");
			
			Topic scoTopic = cli.getCmsRepositoryEntityFactory().newTopic();
			scoTopic.setName("scormContentDesignation");
			scoTopic.setAllowsReferrerContentObjects(true);
			scoTopic.setOwner(systemUser);
			scoTopic.setTaxonomy(cdTax);
			scoTopic.addLocalizedLabel("en", "SCORM Content Designation");
			scoTopic.addLocalizedLabel("el", "SCORM Content Designation");
			scoTopic = cli.getTopicService().saveTopic(scoTopic);
			System.out.println("SCORM Content Designation created successfully!");
		}
		
		Taxonomy fileTypeTax = cli.getTaxonomyService().getTaxonomy("fileResourceContentType", "el");
		if (fileTypeTax == null) {
			System.out.println("fileResourceContentType taxonomy is null, creating ...");
			fileTypeTax = cli.getCmsRepositoryEntityFactory().newTaxonomy();
			fileTypeTax.setName("fileResourceContentType");
			fileTypeTax.addLocalizedLabel("en", "fileResourceContentType");
			fileTypeTax.addLocalizedLabel("el", "fileResourceContentType");
			fileTypeTax = cli.getTaxonomyService().saveTaxonomy(fileTypeTax);
			System.out.println("fileResourceContentType Taxonomy created successfully!");
			
			Topic zipTopic = cli.getCmsRepositoryEntityFactory().newTopic();
			zipTopic.setName("zipType");
			zipTopic.setAllowsReferrerContentObjects(true);
			zipTopic.setOwner(systemUser);
			zipTopic.setTaxonomy(fileTypeTax);
			zipTopic.addLocalizedLabel("en", "ZIP Type");
			zipTopic.addLocalizedLabel("el", "ZIP Type");
			zipTopic = cli.getTopicService().saveTopic(zipTopic);
			System.out.println("ZIP Type created successfully!");
		}
		
		
		// delete taxonomies
		/*if (cdTax != null) {
			cli.getTaxonomyService().deleteTaxonomyTree(cdTax.getId());
			System.out.println("Successfully Deleted contentDesignation Taxonomy Tree");
		}
		
		if (fileTypeTax != null) {
			cli.getTaxonomyService().deleteTaxonomyTree(fileTypeTax.getId());
			System.out.println("Successfully deleted fileResourceContentType Taxonomy Tree");
		}*/
		
		
	}

}
