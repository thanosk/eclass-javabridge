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
require_once("http://localhost:8080/javaBridge/java/Java.inc");

$System = new JavaClass("java.lang.System");
$props = $System->getProperties();
echo "Using java VM from: ${props['java.vm.vendor']}\n";

if(!java_values($props['java.vm.vendor']->toLowerCase()->startsWith("sun")))
  echo "WARNING: You need to run this example with the SUN VM\n";


$jndiProps = array(
  "java.naming.factory.initial" => "org.jnp.interfaces.NamingContextFactory",
  "java.naming.factory.url.pkgs" => "org.jboss.naming:org.jnp.interfaces",
  "java.naming.provider.url" => "127.0.0.1"
);

$ctx = new java("javax.naming.InitialContext", $jndiProps);

$calculator = $ctx->lookup("CalculatorBean/remote");

echo $calculator->add(1,1);
echo $calculator->subtract(1,1);

?>
