<?php
if(isset($_GET["url"]))
{
$url = $_GET["url"];
$filename = "serverdata\page_" . randoms() . ".txt";
$confidential = false;
if(isset($_GET["token"]))
{
	$confidential = true;
	$username = urlencode($_GET["user"]);
	$password = urlencode($_GET["pass"]);
	$url = "http://vk-oauth-proxy.xtrafrancyz.net:80/token?grant_type=password&client_id=2685278&client_secret=lxhD8OD7dMsqtXIm5IUY&username=".
	$username.
	"&password=".
	$password.
	"&scope=notify%2Cfriends%2Cphotos%2Caudio%2Cvideo%2Cdocs%2Cnotes%2Cpages%2Cstatus%2Coffers%2Cquestions%2Cwall%2Cgroups%2Cmessages%2Cnotifications%2Cstats%2Cads%2Coffline";
	$filename = "serverdata\token_" . randoms() . ".txt";
}
$fp = fopen($filename, "w");
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_FILE, $fp);
if(isset($_GET["kate"]))
{
	curl_setopt($ch, CURLOPT_USERAGENT, "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
}
curl_exec($ch);
if(curl_error($ch))
{
    fwrite($fp, curl_error($ch));
}
curl_close($ch);
fclose($fp);
echo file_get_contents($filename);
unlink($filename);
}
else
{
http_response_code(400);
}

function randoms($str = true)
{
	if($str == true)
	{
		$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $randstring = '';
        for ($i = 0; $i < 10; $i++)
		{
            $randstring .= $characters[rand(0, strlen($characters)-1)];
        }
        return $randstring;
	}
	else
	{
		return rand(0, 32000);
	}
}
?>