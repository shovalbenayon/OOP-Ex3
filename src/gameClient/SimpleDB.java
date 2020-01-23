	package gameClient;
	import utils.Range;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.ArrayList;

	/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser = "student";
	public static final String jdbcUserPassword = "OOP2020student";
	private int howMuch;
	/**
	 * simply prints all the games as played by the users (in the database).
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while (resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID") + "," + resultSet.getInt("levelID") + "," + resultSet.getInt("moves") + "," + resultSet.getDate("time") + ", " + resultSet.getInt("score"));
			}
			resultSet.close();
			statement.close();
			connection.close();
			} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 *
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID=" + id + ";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if (resultSet != null && resultSet.next()) {
				ans = resultSet.getString("kml_" + level);
			}
			} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	/**
	 *
	 * @return
	 */
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while (resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static Object[][] printUserLog(int ID_1) {
		int n = getNumOfGames(ID_1);
		Object[][] allUserData = new Object[n][6];

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			int i = 0;
			while (resultSet.next()) {
				int ID = resultSet.getInt("UserID");
				if (ID == ID_1) {
					allUserData[i][0] = "" + (i + 1);
					allUserData[i][1] = "" + ID;
					allUserData[i][2] = "" + resultSet.getInt("levelID");
					allUserData[i][3] = "" + resultSet.getInt("score");
					allUserData[i][4] = "" + resultSet.getInt("moves");
					allUserData[i][5] = "" + resultSet.getDate("time");
					i++;
				}
			}
			resultSet.close();
			statement.close();
			connection.close();
			} catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
		}
		return allUserData;
	}

	public static ArrayList<Student> getRankingForLevel(int level) {
		ArrayList<Student> levelRanking = new ArrayList<Student>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while (resultSet.next()) {
				int levelId = resultSet.getInt("levelID");
					if (levelId == level) {
						levelRanking.add(new Student(resultSet.getInt("UserID"), resultSet.getInt("score"), resultSet.getInt("moves")));
					}
			}
			levelRanking.sort(null);

			resultSet.close();
			statement.close();
			connection.close();
			} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return rmDuplicates(levelRanking);
	}

	public static ArrayList<Student> rmDuplicates(ArrayList<Student> ranking) {
		ArrayList<Student> noDuplList = new ArrayList<Student>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (Student trip : ranking) {
			int id = trip.getID();
			if (!idList.contains(id)) {
				idList.add(id);
				noDuplList.add(trip);
			}
		}
		return noDuplList;
	}

	public static Range Ranking(ArrayList<Student> ranking, int id1) {
		int my_rank = 0;
		int all_users = 0;
		for (Student stu : ranking) {
			if (stu.getID() == id1)
				my_rank = ranking.indexOf(stu) + 1;
			all_users++;
		}
		return new Range(my_rank , all_users);
	}

	public static int getNumOfGames(int ID_1) {
		int numOfGames = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while (resultSet.next()) {
				int ID = resultSet.getInt("UserID");
					if (ID == ID_1) {
						numOfGames++;
					}

			}
			resultSet.close();
			statement.close();
			connection.close();
			} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
			e.printStackTrace(); }
		return numOfGames;
	}

		/**
		 * This method returns the best score of every level the user passed
		 * @param id - id of user
		 * @return table of the data
		 */
	public static Object[][] bestScores(String id) {

		Object[][] arr = new Object[24][2];
		for (int i = 0; i <= 24; i++) {
			double max = -1;
			String CustomersQuery = ("SELECT * FROM Logs where userID="+id+" AND levelID=" + i);
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(CustomersQuery);
				while (resultSet.next()) {
					if (resultSet.getDouble("score") > max) {
						max = resultSet.getDouble("score");
					}
				}
				if (max != -1) { // should be with maxMove
					arr[i][0] = i;
					arr[i][1] = max;
				}
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}

		/**
		 * This Method returns the Higher level the user reached
		 * @param id - id of the user
		 * @return
		 */
	public static int showMaxLevel(String id) {
		int maxLevel = -1;
		String CustomersQuery = "SELECT * FROM Logs where userId=" + id;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(CustomersQuery);
			while (resultSet.next()) {
				if (resultSet.getInt("levelID") > maxLevel) {
					maxLevel = resultSet.getInt("levelID");
				}
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return maxLevel;
	}

	public static int HowMuch(int ID_1){
		int i = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while (resultSet.next()) {
				int ID = resultSet.getInt("UserID");
				if (ID == ID_1) {
					i++;
				}
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return i;

	}
}
		
