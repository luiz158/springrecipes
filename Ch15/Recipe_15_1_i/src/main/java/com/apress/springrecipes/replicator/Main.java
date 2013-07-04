package com.apress.springrecipes.replicator;

import java.lang.management.ManagementFactory;

import java.io.IOException;

import javax.management.Descriptor;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext context =
            new GenericXmlApplicationContext("beans-jmx.xml");

        FileReplicator documentReplicator =
            (FileReplicator) context.getBean("documentReplicator");

        try {
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("bean:name=documentReplicator");

            RequiredModelMBean mbean = new RequiredModelMBean();
            mbean.setManagedResource(documentReplicator, "objectReference");

            Descriptor srcDirDescriptor = new DescriptorSupport(new String[] {
                    "name=SrcDir", "descriptorType=attribute",
                    "getMethod=getSrcDir", "setMethod=setSrcDir" });
            ModelMBeanAttributeInfo srcDirInfo = new ModelMBeanAttributeInfo(
                    "SrcDir", "java.lang.String", "Source directory",
                    true, true, false, srcDirDescriptor);

            Descriptor destDirDescriptor = new DescriptorSupport(new String[] {
                    "name=DestDir", "descriptorType=attribute",
                    "getMethod=getDestDir", "setMethod=setDestDir" });
            ModelMBeanAttributeInfo destDirInfo = new ModelMBeanAttributeInfo(
                    "DestDir", "java.lang.String", "Destination directory",
                    true, true, false, destDirDescriptor);

            ModelMBeanOperationInfo getSrcDirInfo = new ModelMBeanOperationInfo(
                    "Get source directory",
                    FileReplicator.class.getMethod("getSrcDir"));
            ModelMBeanOperationInfo setSrcDirInfo = new ModelMBeanOperationInfo(
                    "Set source directory",
                    FileReplicator.class.getMethod("setSrcDir", String.class));
            ModelMBeanOperationInfo getDestDirInfo = new ModelMBeanOperationInfo(
                    "Get destination directory",
                    FileReplicator.class.getMethod("getDestDir"));
            ModelMBeanOperationInfo setDestDirInfo = new ModelMBeanOperationInfo(
                    "Set destination directory",
                    FileReplicator.class.getMethod("setDestDir", String.class));
            ModelMBeanOperationInfo replicateInfo = new ModelMBeanOperationInfo(
                    "Replicate files",
                    FileReplicator.class.getMethod("replicate"));

            ModelMBeanInfo mbeanInfo = new ModelMBeanInfoSupport(
                    "FileReplicator", "File replicator",
                    new ModelMBeanAttributeInfo[] { srcDirInfo, destDirInfo },
                    null,
                    new ModelMBeanOperationInfo[] { getSrcDirInfo, setSrcDirInfo,
                            getDestDirInfo, setDestDirInfo, replicateInfo },
                    null);
            mbean.setModelMBeanInfo(mbeanInfo);

            mbeanServer.registerMBean(mbean, objectName);
        } catch (JMException e) {
            System.err.println(e);
        } catch (InvalidTargetObjectTypeException e) {
	    System.err.println(e);
        } catch (NoSuchMethodException e) {
	    System.err.println(e);
        }

        System.in.read();

    }
}
