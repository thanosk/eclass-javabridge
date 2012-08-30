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
require_once("http://localhost:8080/JavaBridgeTemplate554/java/Java.inc");
$System = java("java.lang.System");
$System->out->println("lala koko");
$System->err->println("lala koko");

$LogFactory = java("org.apache.commons.logging.LogFactory");
//$logger = java("org.apache.commons.logging.Log");
$LogFactory->getLog("koko")->info("lala");
$LogFactory->getLog("koko")->debug("lala");
$LogFactory->getLog("koko")->error("lala");

echo $System->currentTimeMillis();
echo "\n";
echo java_values($System->currentTimeMillis());
?>
