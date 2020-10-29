import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class my_player 
{
	public static void main(String args[]) throws IOException
	{
		int[][] arrPrevious = new int[5][5];
		int[][] arrNow = new int[5][5];
		int num = readFile(arrPrevious, arrNow);
		int[] ans = search(arrPrevious, arrNow, num);
		outputData(ans);
	}
	private static int[] search(int[][] arrPrevious, int[][] arrNow, int player)
	{
		int[] ans = new int[2];
		ans[0] = -1;
		ans[1] = -1;
		int opposite = 0;
		if (player == 1)
		{
			opposite = 2;
		}
		else
		{
			opposite = 1;
		}
		maxValue(arrPrevious, arrNow, ans, player, opposite, -10000, 10000, 4);
		return ans;
	}
	private static double maxValue(int[][] arrPrevious, int[][] arrNow, int[] ans, int player, int opposite, double a, double b, int depth)
	{
		if (depth == 0)
		{
			return utilityValue(arrNow, player, opposite);
		}
		int count = 0;
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arrNow[i][j] == 0)
				{
					count++;
				}
			}
		}
		if (count == 25)
		{
			ans[0] = 2;
			ans[1] = 2;
			return 0;
		}
		double v = -10000;
		int[][] cannotMove = new int[5][5];
		checkKO(arrPrevious, arrNow, cannotMove, player, opposite);
		ArrayList<int[]> list = validMoves(arrNow, player);
		for(int i = 0; i < list.size(); i++)
		{
			int[] p = list.get(i);
			if (arrNow[p[0]][p[1]] == 0 && cannotMove[p[0]][p[1]] == 0)
			{
				int[][] tmp = tryMove(arrNow, player, p[0], p[1]);
				int[] array = new int[2];
				v = Math.max(v, minValue(arrNow, tmp, array, opposite, player, a, b, depth - 1));
				if (v >= b)
				{
					return v;
				}
				if (a < v)
				{
					a = v;
					ans[0] = p[0];
					ans[1] = p[1];
				}
			}
		}
		return v;
	}
	private static double minValue(int[][] arrPrevious, int[][] arrNow, int[] ans, int player, int opposite, double a, double b, int depth)
	{
		if (depth == 0)
		{
			return utilityValue(arrNow, opposite, player);
		}
		double v = 10000;
		int[][] cannotMove = new int[5][5];
		checkKO(arrPrevious, arrNow, cannotMove, player, opposite);
		ArrayList<int[]> list = validMoves(arrNow, player);
		for(int i = 0; i < list.size(); i++)
		{
			int[] p = list.get(i);
			if (arrNow[p[0]][p[1]] == 0 && cannotMove[p[0]][p[1]] == 0)
			{
				int[][] tmp = tryMove(arrNow, player, p[0], p[1]);
				int[] array = new int[2]; 
				v = Math.min(v, maxValue(arrNow, tmp, array, opposite, player, a, b, depth - 1));
				if (v <= a)
				{
					return v;
				}
				if (b > v)
				{
					b = v;
					ans[0] = p[0];
					ans[1] = p[1];
				}
			}
		}
		return v;
	}
	private static void checkKO(int[][] arrPrevious, int[][] arrNow, int[][] cannotMove, int player, int opposite)
	{
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arrNow[i][j] == 0)
				{
					int[][] tmp = tryMove(arrNow, player, i, j);
					if (check(arrPrevious, tmp) || check(arrNow, tmp))
					{
						cannotMove[i][j] = 1;
					}
				}
			}
		}
	}
	private static boolean check(int[][] arr, int[][] tmp)
	{
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arr[i][j] != tmp[i][j])
				{
					return false;
				}
			}
		}
		return true;
	}
	private static double utilityValue(int[][] arr, int player, int opposite)
	{
		int num1 = 0;
		int num2 = 0;
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arr[i][j] == 1)
				{
					num1++;
				}
				if (arr[i][j] == 2)
				{
					num2++;
				}
			}
		}
		num1 = num1 - groupCountK(arr, 1, 1);
		num2 = num2 - groupCountK(arr, 2, 1);
		if (player == 2)
		{
			return num2 + 2.5 - num1;
		}
		return num1 - num2 - 2.5;
	}
	private static ArrayList<int[]> validMoves(int[][] arrNow, int player)
	{
		ArrayList<int[]> list = new ArrayList<>();
		int[][] visited = new int[5][5];
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arrNow[i][j] == player)
				{
					ArrayList<int[]> tmp = findLibertyPositions(arrNow, player, i, j);
					if(tmp.size() == 1)
					{
						int[] p = tmp.get(0);
						if (visited[p[0]][p[1]] == 0)
						{
							visited[p[0]][p[1]] = 1;
							list.add(p);
						}
					}
				}
				if(arrNow[i][j] == 3 - player)
				{
					ArrayList<int[]> tmp = findLibertyPositions(arrNow, 3 - player, i, j);
					for(int k = 0; k < tmp.size(); k++)
					{
						int[] p = tmp.get(k);
						if (visited[p[0]][p[1]] == 0)
						{
							visited[p[0]][p[1]] = 1;
							list.add(p);
						}
					}
				}
			}
		}
		Map<String, Integer> map = new HashMap<>();
		PriorityQueue<String> pq = new PriorityQueue<>((s1, s2) -> map.get(s2) - map.get(s1));
		for(int i = 0; i < list.size(); i++)
		{
			int[] p = list.get(i);
			int[][] afterArr = tryMove(arrNow, player, p[0], p[1]);
			arrNow[p[0]][p[1]] = player;
			ArrayList<int[]> diedPieces = findDiedPieces(arrNow, 3 - player);
			arrNow[p[0]][p[1]] = 0;
			String s = p[0] + "" + p[1];
			if (findLiberty(afterArr, player, p[0], p[1]))
			{
				map.put(s, diedPieces.size());
				pq.add(s);
			}
		}
		if (pq.size() == 0)
		{
			for(int i = 0; i < 5; i++)
			{
				for(int j = 0; j < 5; j++)
				{
					
					if (arrNow[i][j] == 0)
					{
						int[][] afterArr = tryMove(arrNow, player, i, j);
						arrNow[i][j] = player;
						ArrayList<int[]> diedPieces = findDiedPieces(arrNow, 3 - player);
						arrNow[i][j] = 0;
						String s = i + "" + j;
						if (findLiberty(afterArr, player, i, j))
						{
							map.put(s, diedPieces.size());
							pq.add(s);
						}
					}
				}
			}
		}
		ArrayList<int[]> ans = new ArrayList<>();
		while(pq.size() != 0)
		{
			int[] p = new int[2];
			String s = pq.poll();
			p[0] = s.charAt(0) - '0';
			p[1] = s.charAt(1) - '0';
			ans.add(p);
		}
		return ans;
	}
	
	
	// return a new board
	private static int[][] tryMove(int[][] arr, int player, int x, int y)
	{
		int[][] tmp = new int[5][5];
		copy(arr, tmp);
		tmp[x][y] = player;
		ArrayList<int[]> list = findDiedPieces(tmp, 3 - player);
		removeDiedPieces(tmp, list);
		return tmp;
	}
	
	
	// find Liberty positions of player group
	private static ArrayList<int[]> findLibertyPositions(int[][] arr, int player, int x, int y)
	{
		ArrayList<int[]> list = findAllayPositions(arr, player, x, y);
		ArrayList<int[]> ans = new ArrayList<>();
		int[][] visited = new int[5][5];
		int[] deltaX = {0, 0, 1, -1};
		int[] deltaY = {-1, 1, 0, 0};
		for(int i = 0; i < list.size(); i++)
		{
			int[] p = list.get(i);
			for(int j = 0; j < 4; j++)
			{
				int newX = p[0] + deltaX[j];
				int newY = p[1] + deltaY[j];
				if (!isBounded(newX, newY))
				{
					continue;
				}
				if (visited[newX][newY] == 1)
				{
					continue;
				}
				if (arr[newX][newY] == 0)
				{
					ans.add(new int[] {newX, newY});
					visited[newX][newY] = 1;
				}
			}
		}
		return ans;
	}
	
	
	// Remove died pieces
	private static void removeDiedPieces(int[][] arr, ArrayList<int[]> ans)
	{
		for(int i = 0; i < ans.size(); i++)
		{
			int[] p = ans.get(i);
			arr[p[0]][p[1]] = 0;
		}
	}
	
	
	// find died pieces of player in board
	private static ArrayList<int[]> findDiedPieces(int[][] arr, int player)
	{
		ArrayList<int[]> ans = new ArrayList<>();
		for(int i = 0; i  < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arr[i][j] == player)
				{
					if (!findLiberty(arr, player, i, j))
					{
						ans.add(new int[] {i, j});
					}
				}
			}
		}
		return ans;
	}
	
	
	// find the liberty of player in x and y
	private static boolean findLiberty(int[][] arr, int player, int x, int y)
	{
		ArrayList<int[]> ans = findAllayPositions(arr, player, x, y);
		int[] deltaX = {0, 0, 1, -1};
		int[] deltaY = {-1, 1, 0, 0};
		for(int i = 0; i < ans.size(); i++)
		{
			int[] p = ans.get(i);
			for(int j = 0; j < 4; j++)
			{
				int newI = p[0] + deltaX[j];
				int newJ = p[1] + deltaY[j];
				if (!isBounded(newI, newJ))
				{
					continue;
				}
				if (arr[newI][newJ] == 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	// find a group of player
	private static ArrayList<int[]> findAllayPositions(int[][] arr, int player, int x, int y)
	{
		ArrayList<int[]> ans = new ArrayList<>();
		Queue<int[]> q = new LinkedList<>();
		int[][] visited = new int[5][5];
		q.offer(new int[] {x, y});
		ans.add(new int[] {x, y});
		visited[x][y] = 1;
		int[] deltaX = {0, 0, 1, -1};
		int[] deltaY = {-1, 1, 0, 0};
		while(!q.isEmpty())
		{
			int[] point = q.poll();
			for(int i = 0; i < 4; i++)
			{
				int[] p = new int[2];
				p[0] = point[0] + deltaX[i];
				p[1] = point[1] + deltaY[i];
				if(!isBounded(p[0], p[1]))
				{
					continue;
				}
				if (visited[p[0]][p[1]] == 1)
				{
					continue;
				}
				if (arr[p[0]][p[1]] == player)
				{
					q.offer(p);
					visited[p[0]][p[1]] = 1;
					ans.add(p);
				}
			}
		}
		return ans;
	}
	private static boolean isBounded(int x, int y)
	{
		if (x >= 0 && x < 5)
		{
			if (y >= 0 && y < 5)
			{
				return true;
			}
		}
		return false;
	} 
	private static int groupCountK(int[][] arr, int player, int k)
	{
		int num = 0;
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if (arr[i][j] == player)
				{
					ArrayList<int[]> tmp = findLibertyPositions(arr, player, i, j);
					if (tmp.size() <= k)
					{
						num++;
					}
				}
			}
		}
		return num;
	}
	private static int readFile(int[][] arr1, int[][] arr2)
	{
		try (FileReader reader = new FileReader("input"
				+ ".txt");
	             BufferedReader br = new BufferedReader(reader)
	        ) 
	        {
	            String s = br.readLine();
	            int color = s.charAt(0) - '0';
	            String line = "";
	            for(int i = 0; i < 5; i++)
	            {
	            	line = line + br.readLine();
	            }   
	            processArray(arr1, line);
	            line = "";
	            for(int i = 0; i < 5; i++)
	            {
	            	line = line + br.readLine();
	            }
	            processArray(arr2, line);
	            return color;
	        } catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
		return 0;
	}
	private static void outputData(int[] arr) throws IOException
	{
		File file = new File("output.txt");
		if(!file.exists())
		{
			file.createNewFile();
		}
		FileWriter fw = new FileWriter("output.txt", true);
		try (PrintWriter pw = new PrintWriter(fw)) 
		{
		
			if (arr[0] == -1)
			{
				pw.println("PASS");
			}
			else
			{
				pw.println(arr[0] + "," + arr[1]);
			}
		}
	}
	private static void processArray(int[][] arr, String line)
	{
		char[] c = line.toCharArray();
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				arr[i][j] = c[i * 5 + j] - '0';
			}
		}
	}
	private static void copy(int[][] arr, int[][] tmp)
	{
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				tmp[i][j] = arr[i][j];
			}
		}
	}
}