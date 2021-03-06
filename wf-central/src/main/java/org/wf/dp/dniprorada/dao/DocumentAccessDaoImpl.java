package org.wf.dp.dniprorada.dao;


import java.util.Date;
import java.util.List;
import java.util.Random;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.model.DocumentAccess;

@Repository
public class DocumentAccessDaoImpl implements DocumentAccessDao {
	private final String sURL = "https://igov.org.ua/index#";	
	private SessionFactory sessionFactory;
	
	@Autowired
	public DocumentAccessDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String setDocumentLink(Long nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nMS, String sMail) throws Exception{
		DocumentAccess oDocumentAccess = new DocumentAccess();
		oDocumentAccess.setID_Document(nID_Document);
		oDocumentAccess.setDateCreate(new Date());
		oDocumentAccess.setMS(nMS);
		oDocumentAccess.setFIO(sFIO);
		oDocumentAccess.setMail(sMail);
		oDocumentAccess.setTarget(sTarget);
		oDocumentAccess.setTelephone(sTelephone);
		oDocumentAccess.setSecret(generateSecret());
		writeRow(oDocumentAccess);
		StringBuilder osURL = new StringBuilder(sURL);
		//sURL.append("nID_Document=" + nID_Document + "&");
		osURL.append("nID_Access=");
		osURL.append(getIdAccess());
		osURL.append("&");
		osURL.append("sSecret=");
		osURL.append(oDocumentAccess.getSecret());
		return osURL.toString();
	}

	private String generateSecret() {
		// 97-122 small
		// 65-90 big
		// 48-57 number
		StringBuilder os = new StringBuilder();
		Random ran = new Random();
		for (int i = 1; i <= 20; i++) {
			int a = ran.nextInt(3) + 1;
			switch (a) {
			case 1:
				int num = ran.nextInt((57 - 48) + 1) + 48;
				os.append((char) num);
				break;
			case 2:
				int small = ran.nextInt((122 - 97) + 1) + 97;
				os.append((char) small);
				break;
			case 3:
				int big = ran.nextInt((90 - 65) + 1) + 65;
				os.append((char) big);
				break;
			}
		}
		return os.toString();
	}
        
	private String generateAnswer() {
		// 48-57 number
		StringBuilder os = new StringBuilder();
		Random ran = new Random();
		for (int i = 1; i <= 4; i++) {
                    int big = ran.nextInt((90 - 65) + 1) + 65;
                    os.append((char) big);
                    break;
		}
		return os.toString();
	}        

	private void writeRow(DocumentAccess o) {
		Transaction t = null;
		Session s = getSession();
		try{
			t = s.beginTransaction();
			getSession().saveOrUpdate(o);
			t.commit();
		} catch(Exception e){
			t.rollback();
			throw e;
		} finally {
			s.close();
		}
	}

	private Long getIdAccess() throws Exception{
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		try{
			list = getSession().createCriteria(DocumentAccess.class).list();
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return list.get(0).getID();
	}

	@Override
	public DocumentAccess getDocumentLink(Long nID_Access, String sSecret) {
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		try{
                    list = oSession.createSQLQuery("FROM DocumentAccess WHERE nID=? AND sSecret=?").list();
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return list.get(0);
	}

        
	@Override
	public String getDocumentAccess(Long nID_Access, String sSecret) throws Exception {
		Session oSession = getSession();
		List <DocumentAccess> a = null;
		try{
                    //TODO убедиться что все проверяется по этим WHERE
                    a = oSession.createSQLQuery("FROM DocumentAccess WHERE nID=? AND sSecret=?").list();
                    if(a == null || a.isEmpty()){
                        throw new Exception("Access not accepted!");
                    }
                    DocumentAccess o = a.get(0);
                    String sTelephone = o.getTelephone();
                    //TODO Generate random 4xDigits answercode
                    String sAnswer = "1234"; //generateAnswer();
                    //TODO SEND SMS with this code
                    //
                    
                    //o.setDateAnswerExpire(null);
                    o.setAnswer(sAnswer);
                    writeRow(o);
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return "/";
	}

        
	@Override
	public String setDocumentAccess(Long nID_Access, String sSecret, String sAnswer) throws Exception {
		Session oSession = getSession();
		List <DocumentAccess> a = null;
		try{
                    //TODO убедиться что все проверяется по этим WHERE
                    a = oSession.createSQLQuery("FROM DocumentAccess WHERE nID=? AND sSecret=? AND sAnswer=?").list();
                    if(a == null || a.isEmpty()){
                        throw new Exception("Access not accepted!");
                    }
                    //DocumentAccess o = a.get(0);
                    //o.setAnswer(null);
                    //writeRow(o);
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return "/";
	}
        
	//public String setDocumentAccess(Integer nID_Access, String sSecret, String sAnswer) throws Exception;
	//public String getDocumentAccess(Integer nID_Access, String sSecret) throws Exception;
        
        
	private Session getSession(){
		return sessionFactory.openSession();
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}