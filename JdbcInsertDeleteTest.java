import java.sql.*;

public class JdbcInsertDeleteTest {

  public static void main(String[] args) {
    
    String sqlDelete = "DELETE FROM books WHERE id = ?";
    String sqlFullInsert = "INSERT INTO books ( title, author, price, qty ) VALUES ( ?, ?, ?, ?)";
    String sqlPartialInsert = "INSERT INTO books ( title, author ) VALUES ( ?, ? )";
    String sqlSelect = "SELECT * FROM books";

    try (

      Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/ebookshop?useSSL=false", "derekmiddlemiss", "banana" );
      PreparedStatement deletePrepStmt = conn.prepareStatement( sqlDelete );
      PreparedStatement fullInsertPrepStmt = conn.prepareStatement( sqlFullInsert, Statement.RETURN_GENERATED_KEYS );
      PreparedStatement partialInsertPrepStmt = conn.prepareStatement( sqlPartialInsert, Statement.RETURN_GENERATED_KEYS );
      PreparedStatement selectPrepStmt = conn.prepareStatement( sqlSelect );

    ) {

      deletePrepStmt.setInt( 1, 5 );
      System.out.println( "The SQL query is: " + deletePrepStmt.toString() );
      int countDeleted = deletePrepStmt.executeUpdate();
      System.out.println( countDeleted + " records deleted. \n" );

      fullInsertPrepStmt.setString( 1, "The Joy of Jogging" );
      fullInsertPrepStmt.setString( 2, "Frippert" );
      fullInsertPrepStmt.setDouble( 3, 66.66 );
      fullInsertPrepStmt.setInt( 4, 66 );

      System.out.println( "The SQL query is: " + fullInsertPrepStmt.toString() );
      int countInserted = fullInsertPrepStmt.executeUpdate();
      System.out.println(countInserted + " records inserted.");

      try (
        ResultSet generatedKeys = fullInsertPrepStmt.getGeneratedKeys();
        ) {

        while ( generatedKeys.next() ){
          System.out.println( "Record inserted with primary key " + generatedKeys.getLong(1) );
        }

      } catch ( SQLException ex3 ) {
        ex3.printStackTrace();
      }

      try (
        ResultSet rset = selectPrepStmt.executeQuery();
      ) {
        while( rset.next() ) { 
          System.out.println( 
            rset.getInt( "id" ) + ", "
            + rset.getString( "author" ) + ", "
            + rset.getString( "title" ) + ", "
            + rset.getDouble( "price" ) + ", "
            + rset.getInt( "qty" ) );
        }
      } catch( SQLException ex2 ) {

        ex2.printStackTrace();

      }

    } catch ( SQLException ex ) {

      ex.printStackTrace();

    }

  }





}
