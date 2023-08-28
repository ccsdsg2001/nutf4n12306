import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cc
 * @date 2023年08月28日 14:42
 */
public class testxml {

    static String pomPath = "generator/pom.xml";
    static String module = "";

    public static void main(String[] args) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        Document document1 = new SAXReader().read("generator/" + node.getText());
        Node table = document1.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());



        module = node.getText().replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);


        Map<String, Object> param = new HashMap<>();
//        param.put("module", module);
//        param.put("Domain", Domain);
//        param.put("domain", domain);
//        param.put("do_main", do_main);
//        param.put("tableNameCn", tableNameCn);
//        param.put("fieldList", fieldList);
//        param.put("typeSet", typeSet);
//        param.put("readOnly", readOnly);
        System.out.println("组装参数：" + param);

    }
}
