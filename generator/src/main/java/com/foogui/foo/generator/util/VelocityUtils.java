package com.foogui.foo.generator.util;

import com.foogui.foo.generator.domain.DataBase;
import com.foogui.foo.generator.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 模板工具类
 *
 * @author ruoyi
 */
public class VelocityUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "src/main/java";

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "src/main/resources/mapper";

    /**
     * yaml路径
     */
    private static final String YAMl_PATH = "src/main/resources";


    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(Table table) {
        String moduleName = table.getModuleName();
        String packageName = table.getPackageName();
        String functionName = table.getFunctionName();
        DataBase dataBase = table.getDataBase();


        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", table.getTableName());
        velocityContext.put("functionName", StringUtils.isNotBlank(functionName) ? functionName : "XX功能");
        velocityContext.put("ClassName", table.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(table.getClassName()));
        velocityContext.put("moduleName", moduleName);
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", table.getAuthor());
        velocityContext.put("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        velocityContext.put("pkColumn", table.getPk());
        velocityContext.put("columns", table.getColumns());
        velocityContext.put("table", table);

        //设置数据库的相关信息，供yaml中使用
        velocityContext.put("url", dataBase.getHost());
        velocityContext.put("port", dataBase.getPort());
        velocityContext.put("dbname", dataBase.getDbname());
        velocityContext.put("username", dataBase.getUsername());
        velocityContext.put("password", dataBase.getPassword());

        //设置BaseResult和PageResult路径
        velocityContext.put("baseResultPath", CommonConstant.BASE_RESULT_PATH);
        velocityContext.put("pageResultPath", CommonConstant.PAGE_RESULT_PATH);
        velocityContext.put("pageRequestPath", CommonConstant.PAGE_REQUEST_PATH);
        return velocityContext;
    }


    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList() {
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/java/dto.java.vm");
        templates.add("vm/java/vo.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        // pom
        templates.add("vm/pom/pom.xml.vm");
        // common
        templates.add("vm/common/BaseResult.java.vm");
        templates.add("vm/common/PageRequest.java.vm");
        templates.add("vm/common/PageResult.java.vm");
        // boot
        templates.add("vm/boot/Application.java.vm");
        templates.add("vm/boot/application.yaml.vm");
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, Table table) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = table.getPackageName();
        // 模块名
        String moduleName = table.getModuleName();
        // 大写类名
        String className = table.getClassName();

        String projectName = table.getProjectName();
        String javaPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mapperXmlPath = projectName + "/" + MYBATIS_PATH + "/" + moduleName;
        String yamlPath = projectName + "/" + YAMl_PATH;

        if (template.contains("domain.java.vm")) {
            fileName = javaPath + "/domain/" + className + "PO.java";
        } else if (template.contains("mapper.java.vm")) {
            fileName = javaPath + "/mapper/" + className + "Mapper.java";
        } else if (template.contains("service.java.vm")) {
            fileName = javaPath + "/service/" + className + "Service.java";
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = javaPath + "/service/impl/" + className + "ServiceImpl.java";
        } else if (template.contains("controller.java.vm")) {
            fileName = javaPath + "/controller/" + className + "Controller.java";
        } else if (template.contains("mapper.xml.vm")) {
            fileName = mapperXmlPath + "/" + className + "Mapper.xml";
        } else if (template.contains("dto.java.vm")) {
            fileName = javaPath + "/dto/" + className + "DTO.java";
        } else if (template.contains("vo.java.vm")) {
            fileName = javaPath + "/vo/" + className + "VO.java";
        }else if (template.contains("pom.xml.vm")) {
            fileName = projectName +"/"+ "pom.xml";
        }
        //common
        else if (template.contains("BaseResult.java.vm")){
            String BaseResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.BASE_RESULT_PATH, ".", "/");
            fileName = BaseResultPath +"/"+ "BaseResult.java";
        }else if (template.contains("PageResult.java.vm")){
            String pageResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.PAGE_RESULT_PATH, ".", "/");
            fileName = pageResultPath +"/"+ "PageResult.java";
        }else if (template.contains("PageRequest.java.vm")){
            String pageResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.PAGE_REQUEST_PATH, ".", "/");
            fileName = pageResultPath +"/"+ "PageRequest.java";
        }
        // boot
        else if (template.contains("Application.java.vm")){
            fileName = javaPath +"/"+className +"Application.java";
        }else if (template.contains("application.yaml.vm")){
            fileName = yamlPath +"/"+ "application.yaml";
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    public static void initEngine() {
        Properties p = new Properties();
        try {
            // 加载classpath目录下的vm文件
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
