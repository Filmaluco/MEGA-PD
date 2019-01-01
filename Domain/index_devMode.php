<?php
require_once("DB.php");

$db = new DB("localhost", "filmaluc_PD", "root", "mysql");
//echo "url: " . $_GET['url'] . "</br>";
//echo "servers: " . $_GET['servers']. "</br>";
if ($_SERVER['REQUEST_METHOD'] == "GET") {

      if ($_GET['url'] == "servers") {
           if ($_GET['status'] == "active") {
                $serverArray = $db->query('SELECT * FROM `Servers` WHERE `Status`=1');
            }elseif($_GET['status'] == "inactive"){
                 $serverArray = $db->query('SELECT * FROM `Servers` WHERE `Status`=0');
            }else{
                $serverArray = $db->query('SELECT * FROM `Servers`');
            }
            $u = array();
                foreach ($serverArray as $server) {
                    array_push($u, array(   'ID'=>$server['ID'], 
                                            'Name'=>$server['Name'],
                                            'IP'=>$server['IP'],
                                            'Port'=>$server['Port'],
                                            'Status'=>$server['Status'])
                                        );
                }
                
                echo json_encode($u);
                http_response_code(200);
        }

        if ($_GET['url'] == "server") {
           $id = (int) $_GET['id'];


           if(isset($id)){
                $server = $db->query('SELECT * FROM `Servers` WHERE `ID`='.$id)[0];
                echo json_encode($server);
                http_response_code(200);
            }
             http_response_code(300);
        }
        
        if ($_GET['url'] == "delete") {
                $server = $db->query('DELETE FROM `Servers` WHERE 1');
                http_response_code(200);
            }
        
        http_response_code(300);

}
