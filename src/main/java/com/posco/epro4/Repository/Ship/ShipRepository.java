package com.posco.epro4.Repository.Ship;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.posco.epro4.Contoller.PublicMethod.PMethod;
import com.posco.epro4.DTO.Ship.ShipCurSearchListDTO;
import com.posco.epro4.DTO.Ship.ShipSearchInsertedOneDTO;
import com.posco.epro4.DTO.Ship.ShipSearchListDTO;
import com.posco.epro4.DTO.Ship.ShipSearchOneDTO;
import com.posco.epro4.VO.Ship.Ship1VO;
import com.posco.epro4.VO.Ship.Ship2VO;

@Repository
public class ShipRepository {
    private int maxLimit = 10;
    
    @Autowired
    private EntityManagerFactory emf;

    public List<ShipSearchListDTO> shipSearchList(HashMap<String, String> map) {

        List<ShipSearchListDTO> resultList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String  shipment_num        = map.get("shipment_num");
            String  deliver_to_location = map.get("deliver_to_location");
            String  staff_name          = map.get("staff_name");
            String  cost_center         = map.get("cost_center");
            String  item_name           = map.get("item_name");
            Integer page                = PMethod.getStringToInteger(map.get("page"));
            int     fromIdx             = (page-1) * maxLimit;
            
            if(fromIdx < 0) return resultList;

            String jpql = "select distinct new com.posco.epro4.DTO.Ship.ShipSearchListDTO("
                        + "     scc1.scc1_id, scc1.shipment_num,"
                        + "     po1.po_num, po1.comments,"
                        + "     staff.name,"
                        + "     scc1.deliver_to_location, scc1.send_date,"
                        + "     ( select sum(scc2.quantity_ordered)"
                        + "       from scc2"
                        + "       where scc2.scc1_id = scc1.scc1_id )"
                        + " )"

                        + " from Scc1VO scc1"
                        + " join Po1VO po1 on (po1.po_header_id = scc1.po_header_id)"
                        + " join Scc2VO scc2 on (scc2.scc1_id = scc1.scc1_id)"
                        + " join Po2VO po2 on (po2.po_line_id = scc2.po_line_id)"
                        + " join StaffVO staff on (staff.id = scc1.employee_number)"
                        + " join ItemVO item on (item.item_id = po2.item_id)"

                        + " where ( :shipment_num is null or scc1.shipment_num like :shipment_num2 )"
                        + "   and ( :deliver_to_location is null or scc1.deliver_to_location like :deliver_to_location2 )"
                        + "   and ( :staff_name is null or staff.name like :staff_name2 )"
                        + "   and ( :cost_center is null or scc2.cost_center like :cost_center2 )"
                        + "   and ( :item_name is null or item.item like :item_name2 )"

                        + " order by scc1.scc1_id desc"
                        ;

            TypedQuery<ShipSearchListDTO> tq = em.createQuery(jpql, ShipSearchListDTO.class)
                                                 .setParameter("shipment_num",        shipment_num)
                                                 .setParameter("deliver_to_location", deliver_to_location)
                                                 .setParameter("staff_name",          staff_name)
                                                 .setParameter("cost_center",         cost_center)
                                                 .setParameter("item_name",           item_name)
                                                 .setParameter("shipment_num2",        "%"+shipment_num+"%")
                                                 .setParameter("deliver_to_location2", "%"+deliver_to_location+"%")
                                                 .setParameter("staff_name2",          "%"+staff_name+"%")
                                                 .setParameter("cost_center2",         "%"+cost_center+"%")
                                                 .setParameter("item_name2",           "%"+item_name+"%")
                                                 .setFirstResult(fromIdx)
                                                 .setMaxResults(maxLimit);

            resultList = tq.getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("shipSearchList Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }

    public List<ShipSearchOneDTO> shipSearchOne(HashMap<String, String> map) {

        List<ShipSearchOneDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String shipment_num = map.get("shipment_num");

            String jpql = "select distinct new com.posco.epro4.DTO.Ship.ShipSearchOneDTO("
                        + "     scc1.scc1_id, scc1.shipment_num, scc1.deliver_to_location,"
                        + "     scc2.scc2_id, scc2.seq, scc2.quantity_ordered, scc2.need_by_date, scc2.comment, scc2.po_distribution_id,"
                        + "     po1.po_header_id, po1.po_num,"
                        + "     po2.po_line_id, po2.unit_price, po2.mat_bpa_agree_qt,"
                        + "     item.item, item.uom, item.description,"
                        + "     staff.name,"
                        + "     ( scc2.quantity_ordered - (select (CASE WHEN sum(ship2.quantity_shipped) is null THEN 0 ELSE sum(ship2.quantity_shipped) END) "
                        + "     AS remaining from Ship2VO ship2 where scc2.scc2_id = ship2.scc2_id) )"
                        + " )"

                        + " from Scc1VO scc1"
                        + " join Scc2VO scc2 on (scc2.scc1_id = scc1.scc1_id)"
                        + " join Po1VO po1 on (po1.po_header_id = scc1.po_header_id)"
                        + " join Po2VO po2 on (po2.po_line_id = scc2.po_line_id)"
                        + " join ItemVO item on (item.item_id = po2.item_id)"
                        + " join StaffVO staff on (staff.id = scc1.employee_number)"

                        + " where ( :shipment_num is null or scc1.shipment_num = :shipment_num )"

                        + "order by scc2.seq asc"
                        ;

            TypedQuery<ShipSearchOneDTO> tq = em.createQuery(jpql, ShipSearchOneDTO.class)
                                                .setParameter("shipment_num", shipment_num);

            resultList = tq.getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("shipSearchOne Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }

     public String shipInsertOne(HashMap<String, String> ship1, List<HashMap<String, String>> ship2List) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            // #region scc1
            // * shipment_num 생성
            String temp_shipment_num = ship1.get("vendor_site_id");
            String jpql = "select ship1.shipment_num from Ship1VO ship1";
                  jpql += " where ship1.shipment_num like :shipment_num";
                  jpql += " order by ship1.ship1_id desc";
            List<String> res = em.createQuery(jpql, String.class)
                                 .setParameter("shipment_num", "%S"+temp_shipment_num+"%")
                                 .setMaxResults(1)
                                 .getResultList();

            if(res.size() > 0) {
                String[] temp = res.get(0).split("-");
                temp_shipment_num = temp[0] + "-" + (PMethod.getStringToInteger(temp[1]) + 1);
            } else {
                temp_shipment_num = "S" + temp_shipment_num + "-" + "1";
            }

            String  shipment_num          = temp_shipment_num;
            String  contact_name          = ship1.get("contact_name");
            String  note_to_receiver      = ship1.get("note_to_receiver");
            Date    expected_receipt_date = PMethod.getDateFromString(ship1.get("expected_receipt_date"));
            Date    shipped_date          = PMethod.getDateFromString(ship1.get("shipped_date"));
            Date    send_date             = new Date();
            Integer po_header_id          = PMethod.getStringToInteger(ship1.get("po_header_id"));
            Integer po_release_id         = PMethod.getStringToInteger(ship1.get("po_release_id"));
            Integer scc1_id               = PMethod.getStringToInteger(ship1.get("scc1_id"));

            Ship1VO ship1vo = new Ship1VO();
            ship1vo.setShipment_num(shipment_num);
            ship1vo.setContact_name(contact_name);
            ship1vo.setNote_to_receiver(note_to_receiver);
            ship1vo.setExpected_receipt_date(expected_receipt_date);
            ship1vo.setShipped_date(shipped_date);
            ship1vo.setSend_date(send_date);
            ship1vo.setPo_header_id(po_header_id);
            ship1vo.setPo_release_id(po_release_id);
            ship1vo.setScc1_id(scc1_id);

            em.persist(ship1vo);
            // #endregion scc1


            // #region ship2
            int seq = 1;
            for (HashMap<String,String> ship2 : ship2List) {
                
                Ship2VO ship2vo = new Ship2VO();
                ship2vo.setSeq(seq++);
                ship2vo.setQuantity_shipped(PMethod.getStringToInteger(ship2.get("quantity_shipped")));
                ship2vo.setPo_line_id(PMethod.getStringToInteger(ship2.get("po_line_id")));
                ship2vo.setPo_line_location_id(PMethod.getStringToInteger(ship2.get("po_line_location_id")));
                ship2vo.setPo_distribution_id(PMethod.getStringToInteger(ship2.get("po_distribution_id")));
                ship2vo.setScc1_id(ship1vo.getScc1_id());
                ship2vo.setScc2_id(PMethod.getStringToInteger(ship2.get("scc2_id")));
                ship2vo.setShip1_id(ship1vo.getShip1_id());

                em.persist(ship2vo);
            }
            // #endregion ship2

            tx.commit();

            return shipment_num;

        } catch (Exception e) {

            tx.rollback();
            e.printStackTrace();
            System.out.println("!!! shipInsertOne Error !!!");
            
        } finally {
            em.close();
        }

        return null;
    }

    
    public Object shipCurSearchList(HashMap<String, String> map) {

        List<ShipCurSearchListDTO> resultList = null;
        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String  shipment_num        = map.get("shipment_num");
            String  contact_name        = map.get("contact_name");
            String  deliver_to_location = map.get("deliver_to_location");
            String  subinventory        = map.get("subinventory");
            String  item_name           = map.get("item_name");
            Integer page                = PMethod.getStringToInteger(map.get("page"));

            String jpql = "select distinct new com.posco.epro4.DTO.Ship.ShipCurSearchListDTO("
                        + "     ship1.shipment_num, ship1.send_date, ship1.note_to_receiver, ship1.contact_name, "
                        + "     scc1.deliver_to_location, "
                        + "     po5.destination_subinventory "
                        + ")"
            
                        + "from Ship1VO ship1 "
                        + "join Scc1VO scc1 on scc1.scc1_id = ship1.scc1_id "
                        + "join Ship2VO ship2 on ship2.ship1_id = ship1.ship1_id "
                        + "join Po2VO po2 on po2.po_line_id = ship2.po_line_id "
                        + "join Po5VO po5 on po5.po_distribution_id = ship2.po_distribution_id "
                        + "join ItemVO item on item.item_id = po2.item_id "

                        + "where po5.destination_subinventory is not null "
                        + "and ( :shipment_num is null or ship1.shipment_num like :shipment_num2 ) "
                        + "and ( :contact_name is null or ship1.contact_name like :contact_name2 ) "
                        + "and ( :deliver_to_location is null or scc1.deliver_to_location like :deliver_to_location2 ) "
                        + "and ( :subinventory is null or po5.destination_subinventory like :subinventory2 ) "
                        + "and ( :item_name is null or item.item like :item_name2 ) "

                        + "order by ship1.shipment_num desc "
                        ;

            resultList = em.createQuery(jpql, ShipCurSearchListDTO.class)
                           .setParameter("shipment_num",        shipment_num)
                           .setParameter("contact_name",        contact_name)
                           .setParameter("deliver_to_location", deliver_to_location)
                           .setParameter("subinventory",        subinventory)
                           .setParameter("item_name",           item_name)
                           .setParameter("shipment_num2",        "%"+shipment_num+"%")
                           .setParameter("contact_name2",        "%"+contact_name+"%")
                           .setParameter("deliver_to_location2", "%"+deliver_to_location+"%")
                           .setParameter("subinventory2",        "%"+subinventory+"%")
                           .setParameter("item_name2",           "%"+item_name+"%")
                           .getResultList();

            // 중복 데이터 필터
            List<String> selected_num_list = new ArrayList<>();
            List<ShipCurSearchListDTO> filteredData = new ArrayList<ShipCurSearchListDTO>();
            for(int i = 0; i < resultList.size(); i++){
                ShipCurSearchListDTO dto = resultList.get(i);
                String num = dto.getShipment_num();
                if(!selected_num_list.contains(num)) {
                    selected_num_list.add(num);
                    filteredData.add(dto);
                }
            }


            // paging
            List<ShipCurSearchListDTO> sendData = new ArrayList<ShipCurSearchListDTO>();
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
            System.out.println("shipCurSearchList Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }


    public Object shipSearchInsertedOne(HashMap<String, String> map) {

        EntityManager em = emf.createEntityManager();
        
        System.out.println("map : " + map.toString());

        try{

            String shipment_num = map.get("shipment_num");

            String jpql = "select distinct new com.posco.epro4.DTO.Ship.ShipSearchInsertedOneDTO("
                        + "     ship1.shipment_num, ship1.shipped_date, ship1.expected_receipt_date, "
                        + "     ship1.contact_name, ship1.note_to_receiver, "
                        + "     scc1.deliver_to_location, scc1.comment, "
                        + "     ship2.quantity_shipped, "
                        + "     scc2.quantity_ordered, scc2.need_by_date, "
                        + "     po2.unit_price, "
                        + "     staff.name, "
                        + "     item.item, item.uom, item.description "
                        + ")"
            
                        + "from Ship1VO ship1 "
                        + "join Ship2VO ship2 on ship2.ship1_id = ship1.ship1_id "
                        + "join Scc1VO scc1 on scc1.scc1_id = ship1.scc1_id "
                        + "join Scc2VO scc2 on scc2.scc2_id = ship2.scc2_id "
                        + "join Po2VO po2 on po2.po_line_id = ship2.po_line_id "
                        + "join ItemVO item on item.item_id = po2.item_id "
                        + "join StaffVO staff on staff.id = scc1.employee_number "
            
                        + "where ( :shipment_num is null or ship1.shipment_num = :shipment_num ) "
                        ;

            List<ShipSearchInsertedOneDTO> resultList = em.createQuery(jpql, ShipSearchInsertedOneDTO.class)
                                                          .setParameter("shipment_num", shipment_num)
                                                          .getResultList();

            return resultList;

        } catch(Exception e) {
            System.out.println("shipSearchInsertedOne Error !!!");
            e.printStackTrace();

        } finally {
            em.close();
        }

        return null;
    }
}
