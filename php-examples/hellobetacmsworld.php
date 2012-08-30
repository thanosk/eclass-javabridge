<?php
/*========================================================================
*   Open eClass 2.1
*   E-learning and Course Management System
* ========================================================================
*  Copyright(c) 2003-2009  Greek Universities Network - GUnet
*
*  Developers Group: Thanos Kyritsis
*
*  Open eClass is an open platform distributed in the hope that it will
*  be useful (without any warranty), under the terms of the GNU (General
*  Public License) as published by the Free Software Foundation.
*
*  Contact address: 	GUnet Asynchronous eLearning Group,
*  			Network Operations Center, University of Athens,
*  			Panepistimiopolis Ilissia, 15784, Athens, Greece
*  			eMail: info@openeclass.org
* =========================================================================*/ 
?>
<?php
echo "<pre>";
require_once("http://localhost:8080/JavaBridgeTemplate554/java/Java.inc");

$betacmsClient = new Java("org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient", "localhost");

//Retrieve a list of all available repositories managed by connected BetaCMS Server
$availableRepositories = java_values($betacmsClient->getRepositoryService()->getAvailableCmsRepositories());
// echo a list of these available repository ids
foreach ($availableRepositories as $key => $cmsRepository) {
	echo "repo".$key.": " .$cmsRepository->getId() ."\n";
}
        
//Retrieve information about BetaCMS repository with id 'demorepo'
$betacmsRepository = $betacmsClient->getRepositoryService()->getCmsRepository("myrepo");
echo "We retrieved information about BetaCMS repository with id: " .$betacmsRepository->getId() ."\n";

//Check if BetaCMS repository with id "demorepo" is available in connected BetaCMS Server
if (java_values($betacmsClient->getRepositoryService()->isRepositoryAvailable("myrepo"))) {
	echo "demorepo is available\n";
}

//Create user credentials
$pass = new Java("java.lang.String", "yyy");
$passCh = $pass->toCharArray();
$credentials = new Java("org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials", "xxx", $passCh);
        
//Login to repository with ID 'demorepo'
$betacmsClient->login("myrepo", $credentials);


//Retrieve built in Subject Taxonomy
$subjectTaxonomy = $betacmsClient->getTaxonomyService()->getBuiltInSubjectTaxonomy("en");

//Change label for English Locale and save change to repository
$subjectTaxonomy->addLocalizedLabel("en", "BetaCMS Subject Taxonomy");
$betacmsClient->getTaxonomyService()->saveTaxonomy($subjectTaxonomy);

//Search Taxonomy
$myTaxonomy = $betacmsClient->getTaxonomyService()->getTaxonomy("myTaxonomy", "en");

//Delete taxonomy
if (!java_is_null($myTaxonomy)) {
	$betacmsClient->getTaxonomyService()->deleteTaxonomyTree($myTaxonomy->getId());
}


//Create a new content object instance of type 'genericContentResourceObject'
$newContentObject = $betacmsClient->getCmsRepositoryEntityFactory()->newContentObjectForType("genericContentResourceObject", "en");


//Provide owner for content object. Select system repository user
$owner = $betacmsClient->getRepositoryUserService()->getSystemRepositoryUser();
$newContentObject->setOwner($owner);

//Provide a human readable name for new content object
$newContentObject->setSystemName("myNewContentObject");


//Profile properties
//Title
$profileTitleProperty = $newContentObject->getCmsProperty("profile.title");
$profileTitleProperty->setSimpleTypeValue("Title for new ContentOBject");
        
//Language
$profileLanguageProperty = $newContentObject->getCmsProperty("profile.language");
$profileLanguageProperty->setSimpleTypeValue("en");
        
//Accessibility properties
//Notice the method used to add value : addSimpleTypeValue
//This is because property 'accessibility.canBeReadBy' can contain multiple values
$canBeReadByProperty = $newContentObject->getCmsProperty("accessibility.canBeReadBy");
$canBeReadByProperty->addSimpleTypeValue("ALL");
        
$canBeUpdatedByProperty = $newContentObject->getCmsProperty("accessibility.canBeUpdatedBy");
$canBeUpdatedByProperty->addSimpleTypeValue("NONE");
        
$canBeDeletedByProperty = $newContentObject->getCmsProperty("accessibility.canBeDeletedBy");
$canBeDeletedByProperty->addSimpleTypeValue("NONE");
        
$canBeTaggedByProperty = $newContentObject->getCmsProperty("accessibility.canBeTaggedBy");
$canBeTaggedByProperty->addSimpleTypeValue("ALL");


$body = "<b>Body for new  content object</b>";
$bodyProperty = $newContentObject->getCmsProperty("body");
$bodyProperty->setSimpleTypeValue($body);


//Use ContentService to save new instance
//ALWAYS get a reference to method result as new instance is 
//populated with entity identifier. Properties profile.created and profile.modified
//are automatically created as well.
$newContentObject = $betacmsClient->getContentService()->saveContentObject($newContentObject, false);


//You can retrieve content object using its id
$newContentObject = $betacmsClient->getContentService()->getContentObjectById($newContentObject->getId(), java("org.betaconceptframework.betacms.repository.api.model.query.CacheRegion")->FIVE_MINUTES);

//Export ContentObject to XML
//If variable is not re-assigned xml export will reflect 
//unsaved content object status
echo $newContentObject->toXml();


echo "</pre>";
?>
