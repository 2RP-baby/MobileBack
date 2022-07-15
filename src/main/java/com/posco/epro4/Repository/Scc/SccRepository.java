package com.posco.epro4.Repository.Scc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.posco.epro4.Contoller.PublicMethod.PMethod;
import com.posco.epro4.DTO.Scc.SccSearchOneDTO;
import com.posco.epro4.DTO.Scc.SccCurSearchListDTO;
import com.posco.epro4.DTO.Scc.SccSearchInsertedOneDTO;
import com.posco.epro4.DTO.Scc.SccSearchListDTO;
import com.posco.epro4.VO.Scc.Scc1VO;
import com.posco.epro4.VO.Scc.Scc2VO;

@Repository
public class SccRepository {
    private int maxLimit = 10;
    
    @Autowired
    private EntityManagerFactory emf;

    public List<SccSearchListDTO> sccSearchList(HashMap<String, String> map) {

        List<SccSearchListDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String  po_num           = map.get("po_num");
            String  staff_name       = map.get("staff_name");
            String  staff_dept_code  = map.get("staff_dept_code");
            String  subinventory     = map.get("subinventory");
            String  vendor_name      = map.get("vendor_name");
            String  item_name        = map.get("item_name");
            Integer page             = PMethod.getStringToInteger(map.get("page"));
            int     fromIdx          = (page-1) * maxLimit;

            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccSearchListDTO(";
                  jpql += "     po1.po_header_id, po1.po_num, po1.comments,";
                  jpql += "     po4_1.promised_date,";
                  jpql += "     po5.destination_subinventory,";
                  jpql += "     staff.dept_code, staff.name,";
                  jpql += "     vendor.vendor_name";
                  jpql += " )";
                  
                  jpql += " from Po1VO po1";
                  jpql += " join Po2VO po2 on (po2.po_header_id = po1.po_header_id)";
                  jpql += " left join Po4VO po4_1 on (po4_1.po_line_id = po2.po_line_id)";
                  jpql += " left join Po4VO po4_2 on (po4_2.po_line_id = po2.po_line_id)";
                  jpql += "     and po4_1.promised_date < po4_2.promised_date";
                  jpql += " join Po5VO po5 on (po5.po_line_location_id = po4_1.po_line_location_id)";
                  jpql += " join StaffVO staff on (staff.id = po5.request_person_id)";
                  jpql += " join VendorVO vendor on (vendor.vendor_id = po1.vendor_id)";
                  jpql += " join ItemVO item on (item.item_id = po2.item_id)";

                  jpql += " where po4_2.promised_date is null";
                //   jpql += " and po4_1.promised_date is not null";
                  jpql += " and ( :po_num is null or po1.po_num = :po_num )";
                  jpql += " and ( :staff_name is null or staff.name = :staff_name )";
                  jpql += " and ( :staff_dept_code is null or staff.dept_code = :staff_dept_code )";
                  jpql += " and ( :subinventory is null or po5.destination_subinventory = :subinventory )";
                  jpql += " and ( :vendor_name is null or vendor.vendor_name = :vendor_name )";
                  jpql += " and ( :item_name is null or item.item = :item_name )";

                  jpql += " order by po1.po_num desc";

            TypedQuery<SccSearchListDTO> tq = em.createQuery(jpql, SccSearchListDTO.class);
                                         tq.setParameter("po_num",           po_num);
                                         tq.setParameter("staff_name",       staff_name);
                                         tq.setParameter("staff_dept_code",  staff_dept_code);
                                         tq.setParameter("subinventory",     subinventory);
                                         tq.setParameter("vendor_name",      vendor_name);
                                         tq.setParameter("item_name",        item_name);

            resultList = tq.getResultList();

            // return resultList;

            // 중복 데이터 필터
            List<String> selected_num_list = new ArrayList<>();
            List<SccSearchListDTO> filteredData = new ArrayList<SccSearchListDTO>();
            for(int i = 0; i < resultList.size(); i++){
                SccSearchListDTO dto = resultList.get(i);
                String num = dto.getPo_num();
                if(!selected_num_list.contains(num)) {
                    selected_num_list.add(num);
                    filteredData.add(dto);
                }
            }


            // paging
            List<SccSearchListDTO> sendData = new ArrayList<SccSearchListDTO>();
            int size = filteredData.size();
            // out of index
            if(fromIdx >= size || fromIdx < 0) return sendData;
            // 최대 표시 개수보다 적은 경우
            int maxCnt = maxLimit;
            if(size - fromIdx < maxLimit) maxCnt = size % maxLimit;
            for(int i = fromIdx, cnt = 0; i < size && cnt < maxCnt; i++, cnt++) {
                sendData.add(filteredData.get(i));
            }
            return sendData;

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

            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccSearchOneDTO("
                        + "     po1.po_header_id, po1.po_num, po1.comments,"
                        + "     po2.po_line_id, po2.unit_price, po2.mat_bpa_agree_qt, 0,"
                        + "     po5.po_distribution_id, po5.destination_subinventory,"
                        + "     item.item, item.uom, item.description,"
                        + "     vendor.vendor_id, vendor.vendor_name,"
                        + "     ( po2.mat_bpa_agree_qt - (select (CASE WHEN sum(scc2.quantity_ordered) is null THEN 0 ELSE sum(scc2.quantity_ordered) END) "
                        + "     AS remaining from Scc2VO scc2 where po2.po_line_id = scc2.po_line_id)"
                        + " ) )"

                        + " from Po1VO po1"
                        + " join Po2VO po2 on (po2.po_header_id = po1.po_header_id)"
                        + " join Po5VO po5 on (po5.po_line_id = po2.po_line_id)"
                        + " join VendorVO vendor on (vendor.vendor_id = po1.vendor_id)"
                        + " join ItemVO item on (item.item_id = po2.item_id)"

                        + " where ( :po_num is null or po1.po_num = :po_num )"

                        + " order by po2.po_line_id asc";

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


    
    public Object sccCurSearchList(HashMap<String, String> map) {

        List<SccCurSearchListDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String  shipment_num        = map.get("shipment_num");
            String  staff_name          = map.get("staff_name");
            String  deliver_to_location = map.get("deliver_to_location");
            String  subinventory        = map.get("subinventory");
            String  vendor_name         = map.get("vendor_name");
            String  item_name           = map.get("item_name");
            Integer page                = PMethod.getStringToInteger(map.get("page"));

            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccCurSearchListDTO("
                        + "     scc1.shipment_num, scc1.send_date, "
                        + "     po1.comments, "
                        + "     po5.destination_subinventory, "
                        + "     staff.dept_code, staff.name, "
                        + "     vendor.vendor_name "
                        + ")"

                        + "from Scc1VO scc1 "
                        + "join Scc2VO scc2 on scc2.scc1_id = scc1.scc1_id "
                        + "join Po1VO po1 on po1.po_header_id = scc1.po_header_id "
                        + "join Po2VO po2 on po2.po_line_id = scc2.po_line_id "
                        + "join Po5VO po5 on po5.po_distribution_id = scc2.po_distribution_id "
                        + "join StaffVO staff on staff.id = scc1.employee_number "
                        + "join VendorVO vendor on vendor.vendor_id = po1.vendor_id "
                        + "join ItemVO item on item.item_id = po2.item_id "

                        + "where po5.destination_subinventory is not null "
                        + "  and ( :shipment_num is null or scc1.shipment_num = :shipment_num ) "
                        + "  and ( :staff_name is null or staff.name = :staff_name ) "
                        + "  and ( :deliver_to_location is null or scc1.deliver_to_location = :deliver_to_location ) "
                        + "  and ( :subinventory is null or po5.destination_subinventory = :subinventory ) "
                        + "  and ( :vendor_name is null or vendor.vendor_name = :vendor_name ) "
                        + "  and ( :item_name is null or item.item = :item_name ) "

                        + "order by scc1.shipment_num desc "
                        ;

            resultList = em.createQuery(jpql, SccCurSearchListDTO.class)
                                        .setParameter("shipment_num",        shipment_num)
                                        .setParameter("staff_name",          staff_name)
                                        .setParameter("deliver_to_location", deliver_to_location)
                                        .setParameter("subinventory",        subinventory)
                                        .setParameter("vendor_name",         vendor_name)
                                        .setParameter("item_name",           item_name)
                                        .getResultList();

            // return resultList;

            // 중복 데이터 필터
            List<String> selected_num_list = new ArrayList<>();
            List<SccCurSearchListDTO> filteredData = new ArrayList<SccCurSearchListDTO>();
            for(int i = 0; i < resultList.size(); i++){
                SccCurSearchListDTO dto = resultList.get(i);
                String num = dto.getScc1_shipment_num();
                if(!selected_num_list.contains(num)) {
                    selected_num_list.add(num);
                    filteredData.add(dto);
                }
            }


            // paging
            List<SccCurSearchListDTO> sendData = new ArrayList<SccCurSearchListDTO>();
            int fromIdx = (page - 1) * maxLimit;
            int size = filteredData.size();
            // out of index
            if(fromIdx >= size || fromIdx < 0) return sendData;
            // 최대 표시 개수보다 적은 경우
            int maxCnt = maxLimit;
            if(size - fromIdx < maxLimit) maxCnt = size % maxLimit;
            for(int i = fromIdx, cnt = 0; i < size && cnt < maxCnt; i++, cnt++) {
                sendData.add(filteredData.get(i));
            }
            return sendData;

        } catch(Exception e) {
            System.out.println("sccCurSearchList Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }


    public List<SccSearchInsertedOneDTO> sccSearchInsertedOne(HashMap<String, String> map) {

        List<SccSearchInsertedOneDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String shipment_num = map.get("shipment_num");

            String jpql = "select distinct new com.posco.epro4.DTO.Scc.SccSearchInsertedOneDTO("
                        + "     scc1.scc1_id, scc1.shipment_num, scc1.deliver_to_location, scc1.comment, scc1.subinventory,"
                        + "     scc2.scc2_id, scc2.quantity_ordered, scc2.need_by_date, scc2.comment,"
                        + "     po2.unit_price,"
                        + "     item.item, item.uom, item.description,"
                        + "     vendor.vendor_name"
                        + " )"

                        + " from Po1VO po1"
                        + " join Scc1VO scc1 on (scc1.po_header_id = po1.po_header_id)"
                        + " join Scc2VO scc2 on (scc2.scc1_id = scc1.scc1_id)"
                        + " join Po2VO po2 on (po2.po_line_id = scc2.po_line_id)"
                        + " join ItemVO item on (item.item_id = po2.item_id)"
                        + " join VendorVO vendor on (vendor.vendor_id = po1.vendor_id)"

                        + " where ( :shipment_num is null or scc1.shipment_num = :shipment_num )"

                        + " order by scc1.scc1_id desc"
                        ;

            TypedQuery<SccSearchInsertedOneDTO> tq = em.createQuery(jpql, SccSearchInsertedOneDTO.class)
                                                       .setParameter("shipment_num", shipment_num)
                                                       ;
            resultList = tq.getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("sccSearchInsertedOne Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }
}
