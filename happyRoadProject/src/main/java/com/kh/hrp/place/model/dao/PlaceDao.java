package com.kh.hrp.place.model.dao;

import static com.kh.hrp.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.hrp.common.PageInfo;
import com.kh.hrp.common.model.vo.PlaceImage;
import com.kh.hrp.place.model.vo.Place;
import com.kh.hrp.place.model.vo.PlaceSelect;
import com.kh.hrp.place.model.vo.Review;

public class PlaceDao {
   private Properties prop = new Properties();
   public PlaceDao() {
      String filePath = PlaceDao.class.getResource("/db/sql/place-mapper.xml").getPath();
      
      try {
         prop.loadFromXML(new FileInputStream(filePath));
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   public int selectListCount(Connection conn, int userNo) {
      int listCount = 0;
      PreparedStatement pstmt = null;
      ResultSet rset = null;
      String sql = prop.getProperty("selectListCount");
      
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, userNo);
         rset = pstmt.executeQuery();
         
         if(rset.next()) {
            listCount = rset.getInt("count");
         }
         
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         close(rset);
         close(pstmt);
      }
      
      return listCount;
   }

   public ArrayList<Place> placeLikeSelectList(Connection conn, int userNo, PageInfo pi) {
      ArrayList<Place>  placeList = new ArrayList<>();
      
      PreparedStatement pstmt = null;
      ResultSet rset = null;
      
      String sql = prop.getProperty("placeLikeSelectList");
      try {
         pstmt = conn.prepareStatement(sql);
         
         int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
         int endRow = startRow + pi.getBoardLimit() - 1;
         System.out.println(startRow);
         System.out.println(endRow);
         pstmt.setInt(1, userNo);
         pstmt.setInt(2, startRow);
         pstmt.setInt(3, endRow);
         
         rset = pstmt.executeQuery();
         while (rset.next()) {
	         placeList.add(new Place(rset.getInt("PLACE_NO"), 
		                           rset.getString("PLACE_TITLE"), 
		                           rset.getString("PLACE_CONTENT_POINT"), 
		                           rset.getString("PLACE_CONTENT_DETAIL"),  
		                           rset.getString("PLACE_IMAGE_PATH"))
		               );
		      }
         
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         close(rset);
         close(pstmt);
      }
      return placeList;
   }

   public int placeLikeDeleteController(Connection conn, int userNo, int placeNo) {
      int result = 0;
      PreparedStatement pstmt = null;
      
      String sql = prop.getProperty("placeLikeDeleteController");
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, userNo);
         pstmt.setInt(2, placeNo);
         
         result = pstmt.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         close(pstmt);
      }
      
      return result;
   }
   
   public Place selectPlace(Connection conn, int placeNo) {   
      ResultSet rset = null;
      Place p = null;
      
      PreparedStatement pstmt = null;
      String sql = prop.getProperty("selectPlace");
      
      try {
         pstmt = conn.prepareStatement(sql); // 미완성
         pstmt.setInt(1, placeNo);
         
         rset = pstmt.executeQuery();
         if(rset.next()) {
            p = new Place(
                     rset.getInt("PLACE_NO"),
                     rset.getString("PLACE_TITLE"),
                     rset.getString("PLACE_CONTENT_POINT"),
                     rset.getString("PLACE_CONTENT_DETAIL"),
                     rset.getString("PLACE_THEMA"),
                     rset.getString("PLACE_ADDRESS"),
                     rset.getString("PLACE_HOMEPAGE"),
                     rset.getString("PLACE_CONTACT"),
                     rset.getString("PLACE_TIME"),
                     rset.getDate("PLACE_START"),   
                     rset.getDate("PLACE_END")
                  );
         }
         
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         close(rset);
         close(pstmt);
      }
      
      return p;
      
   }
   

   public int increaseCount(Connection conn, int placeNo) {
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");
		
		try {
			pstmt = conn.prepareStatement(sql); //미완성sql
			pstmt.setInt(1, placeNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	   
   }


	public int selectSearchCount(Connection conn, String placeTitle) {

		int SearchCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectSearchCount");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, placeTitle);
			pstmt.setString(1, "%" + placeTitle + "%");
			
			rset = pstmt.executeQuery();

			if (rset.next()) {
				SearchCount = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}

		return SearchCount;
	}

	public ArrayList<Place> selectSearchList(Connection conn, String placeTitle, PageInfo pi) {

		ArrayList<Place> plist = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectSearchList");

		try {
			pstmt = conn.prepareStatement(sql);

			int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
			int endRow = startRow + pi.getBoardLimit() - 1;
			System.out.println(startRow);
			System.out.println(endRow);
			pstmt.setString(1, "%" + placeTitle + "%");
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);

			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				plist.add(new Place(rset.getInt("PLACE_NO"), 
		                           rset.getString("PLACE_TITLE"), 
		                           rset.getString("PLACE_ADDRESS"), 
		                           rset.getString("PLACE_IMAGE_PATH"))
									);
  				}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return plist;

	}
	
   
   public ArrayList<PlaceImage> selectPlaceImageList(Connection conn, int placeNo){
	   ArrayList<PlaceImage> list = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectPlaceImage");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, placeNo);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				PlaceImage pi = new PlaceImage(
								rset.getInt("PLACE_IMAGE_NO"),
								rset.getString("PLACE_IMAGE_CHANGE"),
								rset.getString("PLACE_IMAGE_PATH")
						);

				
				list.add(pi);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;	
   }
   
   
   public ArrayList<Review> selectReviewList(Connection conn, int placeNo){
	   ArrayList<Review> list = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectReviewList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, placeNo);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Review(
							rset.getString("REVIEW_NO"),
							rset.getString("USER_NAME"),
							rset.getString("REVIEW_STAR"),
							rset.getString("REVIEW_CONTENT"),
							rset.getString("REVIEW_CREATE_DATE")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	   
   }
   
   public int reviewListCount(Connection conn, int placeNo) {
		int reviewListCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("reviewListCount");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, placeNo);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				reviewListCount = rset.getInt("count");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return reviewListCount;
	}
   
   public boolean checkLike(Connection conn, int placeNo, int userNo) {
	   boolean isLike = false;
	   PreparedStatement pstmt = null;
	   ResultSet rset = null;
	   String sql = prop.getProperty("checkLike");
	   
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, placeNo);
			pstmt.setInt(2, userNo);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				isLike = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return isLike; 
   }
   
   
   public int placeLikeInsertController(Connection conn, int userNo, int placeNo) {
   	  int result = 0;
      PreparedStatement pstmt = null;
      
      String sql = prop.getProperty("placeLikeInsertController");
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, placeNo);
         pstmt.setInt(2, userNo);
         
         result = pstmt.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
         close(pstmt);
      }
      
      return result;
   }

   public ArrayList<PlaceSelect> mainSelectController(Connection conn) {
      ArrayList<PlaceSelect> plist = new ArrayList<>();
      PreparedStatement pstmt = null;
      ResultSet rset = null;
      
      String sql = prop.getProperty("mainSelectController");
       try {
         pstmt = conn.prepareStatement(sql);
         rset = pstmt.executeQuery();
         
         while(rset.next()) {
            plist.add(new PlaceSelect(rset.getInt("PLACE_NO"),
                  rset.getString("PLACE_TITLE"),
                  rset.getString("PLACE_CONTENT_POINT"),
                  rset.getString("PLACE_THEMA"),
                  rset.getString("PLACE_ADDRESS"),
                  rset.getString("PLACE_TIME"),
                  rset.getString("PLACE_START"),
                  rset.getString("PLACE_END"),
                  rset.getInt("PLACE_COUNT"),
                  rset.getString("PLACE_IMAGE_PATH"),
                  rset.getString("PLACE_IMAGE_CHANGE")
                  ));
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }finally {
         close(rset);
         close(pstmt);
      }
      
      return plist;
   }

   public ArrayList<PlaceSelect> mainCountSearch(Connection conn) {
      ArrayList<PlaceSelect> plist = new ArrayList<>();
      PreparedStatement pstmt = null;
      ResultSet rset = null;
      
      String sql = prop.getProperty("mainCountSearch");
      try {
         pstmt = conn.prepareStatement(sql);
         rset = pstmt.executeQuery();
         
         while(rset.next()) {
            plist.add(new PlaceSelect(rset.getInt("PLACE_NO"),
                  rset.getString("PLACE_TITLE"),
                  rset.getString("PLACE_CONTENT_POINT"),
                  rset.getString("PLACE_THEMA"),
                  rset.getString("PLACE_ADDRESS"),
                  rset.getString("PLACE_TIME"),
                  rset.getString("PLACE_START"),
                  rset.getString("PLACE_END"),
                  rset.getInt("PLACE_COUNT"),
                  rset.getString("PLACE_IMAGE_PATH"),
                  rset.getString("PLACE_IMAGE_CHANGE")
                  ));
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }finally {
	         close(rset);
	         close(pstmt);
	      }
	      
	      return plist;
   }

   public int insertManagerPlace(Connection conn, PlaceSelect ps) {
	   int result = 0;
	   PreparedStatement pstmt = null;
	   String sql = prop.getProperty("insertManagerPlace");
	   
	  try {
	     pstmt = conn.prepareStatement(sql);
	     SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
	     Date placeStart = (Date)formatter.parse(ps.getPlaceStart());
	     Date placeEnd = (Date)formatter.parse(ps.getPlaceEnd());
	     System.out.println(placeStart);
	     System.out.println(placeEnd);
	     pstmt.setString(1, ps.getPlaceTitle());
	     pstmt.setString(2, ps.getPlaceContentPoint());
	     pstmt.setString(3, ps.getPlaceContentDetail());
	     pstmt.setString(4, ps.getPlaceThema());
	     pstmt.setString(5, ps.getPlaceAddress());
	     pstmt.setString(6, ps.getPlaceHomepage());
	     pstmt.setString(7, ps.getPlaceContact());
	     pstmt.setString(8, ps.getPlaceTime());
	     pstmt.setDate(9, placeStart);
	     pstmt.setDate(10, placeEnd);

	     result = pstmt.executeUpdate();
	  } catch (SQLException e) {
	     e.printStackTrace();
	  } catch (ParseException e) {
		e.printStackTrace();
	} finally {
	     close(pstmt);
	  }
	  
	  return result;
   }
   
   public int insertPlaceImage(Connection conn, ArrayList<PlaceImage> list, PlaceSelect ps) {
	   int result = 0;
	   PreparedStatement pstmt = null;
	   String sql = prop.getProperty("insertPlaceImage");
	   
	   try {
		   for (PlaceImage pi : list) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ps.getPlaceNo());
				pstmt.setString(2, pi.getPlaceImageOrigin());
				pstmt.setString(3, pi.getPlaceImageChange());
				pstmt.setString(4, pi.getPlaceImagePath());
				pstmt.setString(5, pi.getPlaceImageDate());
				
				result = pstmt.executeUpdate();
		   }
	   } catch (SQLException e) {
			 e.printStackTrace();
		 } finally {
			 close(pstmt);
		 }
	   
	   return result;
   }

}