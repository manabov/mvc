package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection con;
	PreparedStatement ptmt; 
	ResultSet rs;
	String sql;
	
	// ssssssa
	public BoardDAO() {
		
		try {		
			// Context는 환경정보를 갖고 오는 것.
			Context init = new InitialContext();    //compile
			DataSource ds = (DataSource)init.lookup("java:comp/env/oooo");			
			con = ds.getConnection();
			// 여기서 null이 나오면 도킹에 실패했다는 의미.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int totalCount() {
		int tot = 0; 
		try {
			
			sql = "select count(*) from mvcBoard";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			rs.next();
			tot = rs.getInt(1);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tot;
	}
	
	
	// 전체 목록창에 보여줄 자료들을 꺼내오는 작업. 꼭 ArrayList<BoardVO>로 할 필요는 없다.
	// Object형으로 받아야 할 경우도 있을 것.
	public ArrayList<BoardVO> list(int start, int end) {
		ArrayList<BoardVO> res = new ArrayList<>();
		
		try {
			
			sql = "select * from " + 
				"(select rownum rnum, tt.* from " + 
				"(select * from mvcBoard order by gid desc, seq) tt) " + 
				"where rnum >=? and rnum<=?";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, start);
			ptmt.setInt(2, end); 
			// 마지막 페이지의 경우 개수가 limit보다 적더라도 상관없다.

			rs = ptmt.executeQuery();
			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				
				// list.jsp에서 lev에 따라서 들여쓰기 해야하므로 lev필요함. 
				vo.setLev(rs.getInt("LEV"));
				
				vo.setId(rs.getInt("ID"));
				vo.setTitle(rs.getString("TITLE"));
				vo.setPname(rs.getString("PNAME"));
				vo.setReg_date(rs.getTimestamp("REG_DATE"));
				vo.setUpfile(rs.getString("UPFILE"));
				vo.setCnt(rs.getInt("CNT"));
				res.add(vo);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
	}
	
	public int insert(BoardVO vo) {
		
		try {
			
			// 사실 mbvBoard_SEQ를 쓰지 않고 바로 select max(id)+1 from mbcBoard를 해도 된다.
			// 근데 뭐 알아둬서 나쁠 건 없으니까~
			sql = "insert into mvcBoard(id, gid, seq, lev, cnt, reg_date, pname, pw, title, content, upfile)"+
					" values(mvcBoard_Seq.nextval, mvcBoard_Seq.nextval, 0, 0, -1, sysdate, ?, ?, ?, ?, ?)";
													// 첫 글 조회수는 0이 되어야 하기 때문에 -1으로 한다. (쓰자마자 한번 호출되므로!)
			ptmt = con.prepareStatement(sql);
			
			// 일단 insertForm에서 적은 5가지만 setString한다.
			ptmt.setString(1, vo.getPname());
			ptmt.setString(2, vo.getPw());
			ptmt.setString(3, vo.getTitle());
			ptmt.setString(4, vo.getContent());
			ptmt.setString(5, vo.getUpfile());
			
			ptmt.executeUpdate();
			
			// 글을 업데이트하자마자 List로 가는 것이 아니라 Detail을 보여주도록 하기 위한 작업
			// Sequence를 안 써도 되는 이유.. detail할 때 어차피 max(id)를 쓰기 때문임. 
			sql = "select max(id) from mvcBoard";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			rs.next();
			
			return rs.getInt(1);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return 0;
	}
	
	public void addCount(int id) {
		
		try {
			// 간단한 SQL문~
			sql = "update mvcBoard set cnt = cnt + 1 where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			ptmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} // 얘는 아직 끝나지 않은 녀석이므로 (detail까지 실행해야 함) close()를 하지 않아야 함.
	}
	
	public BoardVO detail(int id) {
		
		try {
			// detail(or modify)화면을 출력하기 위해 필요한 정보를 DB로부터 빼온다.
			// 그 기준값인 id를 매개변수로 받는다.
			sql = "select * from mvcBoard where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			rs = ptmt.executeQuery();
			
			// detail은 하나만 보여주면 되므로 while문이 아닌 if문으로 한다.
			// 위 sql문만 보더라도 한 record 값만 가져오도록 되어있다.
			if(rs.next()) {
				
				BoardVO vo = new BoardVO();
				// 화면에 출력해줄 값들을 DB에서 가져온다..
				vo.setId(id);
				// gid, lev, seq 3개는 Detail일 때는 필요 없지만
				// Reply할 때 호출되는 detail 메소드에서는 필요하다!
				vo.setGid(rs.getInt("GID")); 
				vo.setLev(rs.getInt("LEV"));
				vo.setSeq(rs.getInt("SEQ"));
				
				vo.setCnt(rs.getInt("CNT"));
				vo.setReg_date(rs.getTimestamp("REG_DATE"));
				vo.setPname(rs.getString("PNAME"));
				vo.setTitle(rs.getString("TITLE"));
				vo.setUpfile(rs.getString("UPFILE"));
				vo.setContent(rs.getString("CONTENT"));
				
				// 바로 리턴.
				return vo;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
		// Detail만 생각한다면 여기서 finally를 통해 close()를 호출하는 것이 편하겠지만
		// Reply할 때에는 과정 중에 해당 메소드를 호출하게 되므로 close()를 해서는 안 된다.
		return null;	
	}

	public BoardVO search(BoardVO vo) {
		// 매개변수로 받아오는 VO와 리턴하게 되는 VO가 다르다!
		
		try {
			sql = "select * from mvcBoard where id = ? and pw = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, vo.getId());
			ptmt.setString(2, vo.getPw());
			
			rs = ptmt.executeQuery();
			
			if(rs.next()) {

				BoardVO res = new BoardVO();		
				res.setUpfile(rs.getString("upfile"));
				return res;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void delete(int id) {
		
		try {
			sql = "delete from mvcBoard where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			
			ptmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}// 얘는 close()를 하지 않는다. 될 때도 있고 안 될 때도 있기 때문이다.
	}
	
	public void modify(BoardVO vo) {
		
		try {	
			sql = "update mvcBoard set title=?, pname=?, content=?, upfile=? where id = ?";
			ptmt = con.prepareStatement(sql);
			
			ptmt.setString(1, vo.getTitle());
			ptmt.setString(2, vo.getPname());
			ptmt.setString(3, vo.getContent());
			ptmt.setString(4, vo.getUpfile());
			ptmt.setInt(5, vo.getId());
			
			ptmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fileDelete(int id) {
		try {
			sql = "update mvcBoard set upfile=null where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			ptmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int reply(BoardVO vo) {
		int res = 0;
		
		try {
			// 현재글의 구체적인 정보를 갖고오기 위해 detail()를 호출하여 orig에 저장한다.
			BoardVO orig = detail(vo.getId());
			// detail을 통해 얻어온 객체 orig에 달려있는 기존의 댓글들에 대해 처리해줄 것이 있다.
			// orig에 달린 기존 댓글들의 seq를 1씩 더해준다. 하나씩 아래로 내려야 새로 올리려는 댓글의 자리가 생겨나니까.
			sql = "update mvcBoard set seq = seq+1 where gid = ? and seq > ?";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, orig.getGid()); // 원글과 댓글은 gid가 동일하다.
			ptmt.setInt(2, orig.getSeq());
			// 근데 댓글도 일종의 원글이 될 수가 있다... 그러므로 seq가 매우 중요함.
			
			ptmt.executeUpdate();
			
			// 댓글들의 seq를 하나씩 증가시킴으로써 생긴 빈 공간에다가 나의 댓글을 올린다. 댓글이니까 file은 없다. 
			sql = "insert into mvcBoard(id, gid, seq, lev, reg_date, cnt, pname, pw, title, content) "
					+ "values(mvcBoard_Seq.nextval, ?, ?, ?, sysdate, -1, ?, ?, ?, ? )";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, orig.getGid()); // detail에서 갖고온 녀석이랑 GID는 공유해야 하니까.
			ptmt.setInt(2, orig.getSeq()+1); // detail에사 갖고온 글의 바로 아래여야 하니까 +1
			ptmt.setInt(3, orig.getLev()+1); // 댓글은 lev을 +1 해야 하니까.
			
			// 이하의 String들은 vo가 갖고 있는 값들을 가져와야 한다.
			ptmt.setString(4, vo.getPname());
			ptmt.setString(5, vo.getPw());
			ptmt.setString(6, vo.getTitle());
			ptmt.setString(7, vo.getContent());
			
			ptmt.executeUpdate();
			
			sql = "select max(id) from mvcBoard";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			rs.next();
			res = rs.getInt(1);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println(res);
		return res;
	}
	
	public void close() {	
		if(rs != null) try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		if(ptmt != null) try {ptmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(con != null) try {con.close();} catch (SQLException e) {e.printStackTrace();}	
	}
}
