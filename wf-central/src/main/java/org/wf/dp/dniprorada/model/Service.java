package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 23:10
 */
@javax.persistence.Entity
public class Service extends Entity {

	@JsonProperty(value="sName")
   @Column(name = "sName", nullable = false)
   private String name;

	@JsonProperty(value="nOrder")
   @Column(name = "nOrder", nullable = false)
   private Integer order;

	@JsonProperty(value="nID_Subcategory")
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_Subcategory", nullable = false)
   private Subcategory subcategory;

	@JsonProperty(value="aServiceData")
   @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
   @LazyCollection(LazyCollectionOption.FALSE)
   private List<ServiceData> serviceDataList;

	@JsonProperty(value="sInfo")
   @Column(name = "sInfo", nullable = false)
   private String info;

   @JsonProperty("sFAQ")
   @Column(name = "sFAQ", nullable = false)
   private String faq;

   @JsonProperty("sLaw")
   @Column(name = "sLaw", nullable = false)
   private String law;

   
	@JsonProperty(value="sSubjectOperatorName")
   @Column(name = "sSubjectOperatorName", nullable = false)
   private String sSubjectOperatorName;
   
   
   
   @Transient
   private int sub = 0;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }


   public String getSubjectOperatorName() {
      return sSubjectOperatorName;
   }

   public void setSubjectOperatorName(String s) {
      this.sSubjectOperatorName = s;
   }
   

   @JsonProperty(value = "nSub")
   public int getSub() {
      return sub;
   }

   public void setSub(int n) {
      sub = n;
   }

    
   public Integer getOrder() {
      return order;
   }
   public void setOrder(Integer order) {
      this.order = order;
   }

   public Subcategory getSubcategory() {
      return subcategory;
   }
   public void setSubcategory(Subcategory subcategory) {
      this.subcategory = subcategory;
   }

   @JsonGetter("aServiceData")
   public List<ServiceData> getServiceDataFiltered() {
      if (serviceDataList == null) {
         return null;
      }

      List<ServiceData> res = new ArrayList<>();
      for (ServiceData oServiceData : serviceDataList) {
         if (!oServiceData.isHidden()) {
            res.add(oServiceData);
         }
      }
      return res;
   }

   @JsonIgnore
   public List<ServiceData> getServiceDataList() {
      return serviceDataList;
   }
   public void setServiceDataList(List<ServiceData> serviceDataList) {
      this.serviceDataList = serviceDataList;
   }

   public String getInfo() {
      return info;
   }
   public void setInfo(String info) {
      this.info = info;
   }

   public String getFaq() {
      return faq;
   }
   public void setFaq(String faq) {
      this.faq = faq;
   }

   public String getLaw() {
      return law;
   }
   public void setLaw(String law) {
      this.law = law;
   }
}
