<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>bCMS query eClass Lessons Example</title>
</head>
<body>
<?php
echo "<pre>";
$bridge_host = "localhost";
$bridge_port = "8080";
$bridge_context = "JavaBridgeTemplate554";
$bcms_host = "localhost";
$bcms_port = "8080";
$bcms_repo = "myrepo";
$bcms_user = "xxx";
$bcms_pass = "yyy";
require_once("http://".$bridge_host.":".$bridge_port."/".$bridge_context."/java/Java.inc");

$cli = new Java("org.betaconceptframework.betacms.repository.client.BetaCmsRepositoryClient", $bcms_host.":".$bcms_port);

$availableRepositories = java_values($cli->getRepositoryService()->getAvailableCmsRepositories());
foreach ($availableRepositories as $key => $cmsRepository) {
	echo "repo".$key.": " .$cmsRepository->getId() ."\n";
}
        
$betacmsRepository = $cli->getRepositoryService()->getCmsRepository($bcms_repo);
echo "We retrieved information about BetaCMS repository with id: " .$betacmsRepository->getId() ."\n";

if (java_values($cli->getRepositoryService()->isRepositoryAvailable($bcms_repo))) {
	echo "demorepo is available\n";
}

$pass = new Java("java.lang.String", $bcms_pass);
$passCh = $pass->toCharArray();
$credentials = new Java("org.betaconceptframework.betacms.repository.api.security.BetaCmsCredentials", $bcms_user, $passCh);
$cli->login($bcms_repo, $credentials);

$criteria = java("org.betaconceptframework.betacms.repository.model.factory.CmsCriteriaFactory")->newContentObjectCriteria();
$criteria->addContentObjectTypeEqualsCriterion("eClassLessonObject");
$criteria->setCacheable(java("org.betaconceptframework.betacms.repository.api.model.query.CacheRegion")->FIVE_MINUTES);

//Execute query
$cmsOutcome = $cli->getContentService()->searchContentObjects($criteria);

foreach ($cmsOutcome->getResults() as $key => $cmsRankedOutcome) {
	$co = $cmsRankedOutcome->getCmsRepositoryEntity();
	$contentObjectType = $co->getContentObjectType();
	
	$titleProperty = $co->getCmsProperty("profile.title");
	$lessonDescPR = $co->getCmsProperty("lessonDescription");
	$keywordsPR = $co->getCmsProperty("keywords");
	$copyrightPR = $co->getCmsProperty("copyright");
	$authorsPR = $co->getCmsProperty("authors");
	$projectPR = $co->getCmsProperty("project");
	$commentsPR = $co->getCmsProperty("comments");
	$unitsPR = $co->getCmsPropertyList("units");
	$scosPR = $co->getCmsProperty("scormFiles");
	
	echo "Found content object of type " . $contentObjectType ."\n\n";
	echo "Title: " . $titleProperty->getSimpleTypeValue() ."\n";
	echo "lessonDescription: " . $lessonDescPR->getSimpleTypeValue() ."\n";
	echo "keywords: " . $keywordsPR->getSimpleTypeValue() ."\n";
	echo "copyright: " . $copyrightPR->getSimpleTypeValue() ."\n";
	echo "author: " . $authorsPR->getSimpleTypeValue() ."\n";
	echo "project: " . $projectPR->getSimpleTypeValue() ."\n";
	echo "comments: " . $commentsPR->getSimpleTypeValue() ."\n";
	
	if (!java_is_null($unitsPR) && $unitsPR->size() > 0) {
		echo "=== Units(".$unitsPR->size().") ===" ."\n";
		for ($i = 0; $i < java_values($unitsPR->size()); $i++) {
			$ccprop = $unitsPR->get($i);
			echo "title[".$i."]: " . $ccprop->getChildProperty("title")->getSimpleTypeValue() ."\n";
			echo "description[".$i."]: " . $ccprop->getChildProperty("description")->getSimpleTypeValue() ."\n";
		}
	}
	
	if (!java_is_null(scosPR)) {
		echo "=== SCOs ====" ."\n";
		$bcs = $scosPR->getSimpleTypeValues();
		echo "number of files: " . $bcs->size() ."\n";
		foreach ($bcs as $key => $bc) {
			echo $bc->getSourceFilename() ." ". $bc->getMimeType() ." ".  $bc->getCalculatedSize() ."\n";
		}
	}
			
	//echo $co->toXml();
			
}

echo "</pre>";
?>
</body>
</html>
