<?php
$current = "2.4.0";
$current1 = 2;
$current2 = 4;
$current3 = 0;
$v = $_GET["v"];

$a = explode(".", $v);

if(intval($a[0]) < $current1 || intval($a[1]) < $current2 || intval($a[2]) < $current3)
{
	echo "outdated"
}
else
{
	echo "uptodate";
}
?>