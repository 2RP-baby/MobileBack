package com.posco.epro4.Repository.Scc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.posco.epro4.Contoller.PublicMethod.PMethod;
import com.posco.epro4.DTO.Scc.SccSearchOneDTO;
import com.posco.epro4.DTO.Scc.SccSearchListDTO;
import com.posco.epro4.VO.Scc.Scc1VO;
import com.posco.epro4.VO.Scc.Scc2VO;

@Repository
public class SccRepository {
    
    @Autowired
    private EntityManagerFactory emf;

    public List<SccSearchListDTO> sccSearchList(HashMap<String, String> map) {

        List<SccSearchListDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String po_num           = map.get("po_num");
            String staff_name       = map.get("staff_name");
            String staff_dept_code  = map.get("staff_dept_code");
            String subinventory     = map.get("subinventory");
            String vendor_name      = map.get("vendor_name");
            String item_name        = map.get("item_name");

            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccSearchListDTO(";
                  jpql += " po1.po_num, po1.comments,";
                  jpql += " po4_1.promised_date,";
                  jpql += " po5.destination_subinventory,";
                  jpql += " staff.dept_code, staff.name,";
                  jpql += " vendor.vendor_name";
                  jpql += " )";
                  
                  jpql += " from Po1VO po1";
                  jpql += " join Po2VO po2 on (po2.po_header_id = po1.po_header_id)";
                  jpql += " left join Po4VO po4_1 on (po4_1.po_line_id = po2.po_line_id)";
                  jpql += " left join Po4VO po4_2 on (po4_2.po_line_id = po2.po_line_id)";
                  jpql += "   and po4_1.promised_date < po4_2.promised_date";
                  jpql += " join Po5VO po5 on (po5.po_line_location_id = po4_1.po_line_location_id)";
                  jpql += " join StaffVO staff on (staff.id = po5.request_person_id)";
                  jpql += " join VendorVO vendor on (vendor.vendor_id = po1.vendor_id)";
                  jpql += " join ItemVO item on (item.item_id = po2.item_id)";

                  jpql += " where po4_2.promised_date is null";
                  jpql += " and ( :po_num is null or po1.po_num = :po_num )";
                  jpql += " and ( :staff_name is null or staff.name = :staff_name )";
                  jpql += " and ( :staff_dept_code is null or staff.dept_code = :staff_dept_code )";
                  jpql += " and ( :subinventory is null or po5.destination_subinventory = :subinventory )";
                  jpql += " and ( :vendor_name is null or vendor.vendor_name = :vendor_name )";
                  jpql += " and ( :item_name is null or item.item = :item_name )";

            TypedQuery<SccSearchListDTO> tq = em.createQuery(jpql, SccSearchListDTO.class);
            tq.setParameter("po_num",           po_num);
            tq.setParameter("staff_name",       staff_name);
            tq.setParameter("staff_dept_code",  staff_dept_code);
            tq.setParameter("subinventory",     subinventory);
            tq.setParameter("vendor_name",      vendor_name);
            tq.setParameter("item_name",        item_name);
            // TODO: 10개씩 들고오도록 바꿔야됨
            tq.setMaxResults(10);

            resultList = tq.getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("sccSearchList Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }

    public List<SccSearchOneDTO> sccSearchOne(HashMap<String, String> map) {

        List<SccSearchOneDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String po_num = map.get("po_num");

            // TODO: 잔량 가져와야됨
            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccSearchOneDTO(";
                  jpql += " po1.po_header_id, po1.po_num, po1.comments,";
                  jpql += " po2.po_line_id, po2.unit_price, po2.quantity,";
                  jpql += " po5.po_distribution_id, po5.destination_subinventory,";
                  jpql += " item.item, item.uom, item.description,";
                  jpql += " vendor.vendor_name";
                  jpql += " )";

                  jpql += " from Po1VO po1";
                  jpql += " join Po2VO po2 on (po2.po_header_id = po1.po_header_id)";
                  jpql += " join Po5VO po5 on (po5.po_line_id = po2.po_line_id)";
                  jpql += " join VendorVO vendor on (vendor.vendor_id = po1.vendor_id)";
                  jpql += " join ItemVO item on (item.item_id = po2.item_id)";

                  jpql += " where ( :po_num is null or po1.po_num = :po_num )";

                  jpql += " order by po2.po_line_id asc";

            TypedQuery<SccSearchOneDTO> tq = em.createQuery(jpql, SccSearchOneDTO.class);
            tq.setParameter("po_num", po_num);

            resultList = tq.getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("sccSearchOne Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }

     public String sccInsertOne(HashMap<String, String> scc1, List<HashMap<String, String>> scc2List) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            // #region scc1
            // * shipment_num 생성
            String temp_shipment_num = scc1.get("vendor_site_id");
            String jpql = "select scc1.shipment_num";
                  jpql += " from Scc1VO scc1";
                  jpql += " where scc1.shipment_num like :shipment_num";
                  jpql += " order by scc1.scc1_id desc";
            List<String> res = em.createQuery(jpql, String.class)
                                .setParameter("shipment_num", "%R"+temp_shipment_num+"%")
                                .setMaxResults(1)
                                .getResultList();
            String[] temp = null;
            if(res.size() > 0) {
                temp = res.get(0).split("-");
                temp_shipment_num = temp[0] + "-" + (PMethod.getStringToInteger(temp[1]) + 1);
            } else {
                temp_shipment_num = "R" + temp_shipment_num + "-" + "1";
            }

            String  shipment_num        = temp_shipment_num;
            Integer employee_number     = PMethod.getStringToInteger(scc1.get("employee_number"));
            String  deliver_to_location = scc1.get("deliver_to_location");
            String  comment             = scc1.get("comment");
            Date    send_date           = new Date();
            Integer po_header_id        = PMethod.getStringToInteger(scc1.get("po_header_id"));
            Integer po_release_id       = PMethod.getStringToInteger(scc1.get("po_release_id"));
            String  subinventory        = scc1.get("subinventory");

            Scc1VO scc1vo = new Scc1VO();
            scc1vo.setShipment_num(shipment_num);
            scc1vo.setEmployee_number(employee_number);
            scc1vo.setDeliver_to_location(deliver_to_location);
            scc1vo.setComment(comment);
            scc1vo.setSend_date(send_date);
            scc1vo.setPo_header_id(po_header_id);
            scc1vo.setPo_release_id(po_release_id);
            scc1vo.setSubinventory(subinventory);

            em.persist(scc1vo);
            // #endregion scc1


            // #region scc2
            // PR2 생성
            Date date = null;
            int seq = 1;
            for (HashMap<String,String> scc2 : scc2List) {
                
                Scc2VO scc2vo = new Scc2VO();
                scc2vo.setSeq(seq++);
                scc2vo.setQuantity_ordered(PMethod.getStringToInteger(scc2.get("quantity_ordered")));
                scc2vo.setCost_center(scc2.get("cost_center"));
                scc2vo.setComment(scc2.get("comment"));
                date = PMethod.getDateFromString(scc2.get("need_by_date"));
                scc2vo.setNeed_by_date(date);
                scc2vo.setScc1_id(scc1vo.getScc1_id());
                scc2vo.setPo_line_id(PMethod.getStringToInteger(scc2.get("po_line_id")));
                scc2vo.setPo_distribution_id(PMethod.getStringToInteger(scc2.get("po_distribution_id")));

                em.persist(scc2vo);
            }
            // #endregion scc2

            tx.commit();

            return shipment_num;

        } catch (Exception e) {

            tx.rollback();
            e.printStackTrace();
            System.out.println("!!! sccInsertOne Error !!!");
            
        } finally {
            em.close();
        }

        return null;
    }

}
