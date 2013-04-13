package util;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;
public class AvatarLoader extends JPanel{
	String list;
	static ImageLoader loader;
	LinkedList<String> s=new LinkedList<String>();
	LinkedList<String> b=new LinkedList<String>();
	LinkedList<String> h=new LinkedList<String>();
	LinkedList<String> w=new LinkedList<String>();
	int numShirt,numBody,numHead,numWeapon;
	String[] bodyList={"B007","B003","B004","B005"};
	String[] headList={"H008","H010","H017","H018"};
	String[] weaponList={"W044","W045","W046","W050"};
	int shirtCount=0,weaponCount=0,bodyCount=0,headCount=0;
	static BufferedImage head,body,weapon,shirt,background;	
	public void addToLinkedList(String list){
		String sList=list;
		String hList=list;
		String bList=list;
		String wList=list;
		//add number of body
		numBody=0;
		while(bList.indexOf("B")!=-1){	
			String bodyPath=bList.substring(bList.indexOf("B"),bList.indexOf("B")+4);
			if(bList.length()>=4){
				bList=bList.substring(bList.indexOf("B")+4);
				b.add(bodyPath);
				numBody++;
			}else{
				break;
			}	
		}
		//add number of head
		numHead=0;
		while(hList.indexOf("H")!=-1){	
			String headPath=hList.substring(hList.indexOf("H"),hList.indexOf("H")+4);
			if(hList.length()>=4){
				hList=hList.substring(hList.indexOf("H")+4);
				h.add(headPath);
				numHead++;
			}else{
				break;
			}	
		}


		//add number of shirt
		numShirt=0;
		while(sList.indexOf("S")!=-1){	
			String shirtPath=sList.substring(sList.indexOf("S"),sList.indexOf("S")+4);
			if(sList.length()>=4){
				sList=sList.substring(sList.indexOf("S")+4);
				s.add(shirtPath);
				numShirt++;
			}else{
				break;
			}	
		}
		//System.out.print(numShirt+"");



		//add number of weapon
		numWeapon=0;
		while(wList.indexOf("W")!=-1){	
			String weaponPath=wList.substring(wList.indexOf("W"),wList.indexOf("W")+4);
			if(wList.length()>=4){
				wList=wList.substring(wList.indexOf("W")+4);
				w.add(weaponPath);
				numWeapon++;
			}else{
				break;
			}	
		}

	}
	public static void loadBufferedImage(String H, String B, String S, String W, String BG){
		loader=new ImageLoader();
		head=ImageLoader.loadImageFromLocal(H);
		body= ImageLoader.loadImageFromLocal(B);
		weapon= ImageLoader.loadImageFromLocal(W);
		shirt=ImageLoader.loadImageFromLocal(S);	
		background=ImageLoader.loadImageFromLocal(BG);

	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		list="\"B001\",\"B002\",\"B003\",\"B004\",\"B005\",\"B006\",\"B007\",\"H008\",\"H009\",\"H010\",\"H011\",\"H012\",\"H013\",\"H014\",\"H015\",\"H016\",\"H017\",\"H018\",\"S019\",\"S020\",\"S021\",\"S022\",\"S023\",\"S024\",\"S025\",\"S026\",\"S027\",\"S028\",\"S029\",\"S030\",\"S031\",\"S032\",\"S033\",\"S034\",\"S035\",\"S036\",\"S037\",\"S038\",\"S039\",\"S040\",\"S041\",\"S042\",\"S043\",\"W044\",\"W045\",\"W046\",\"W047\",\"W048\",\"W049\",\"W050\",\"W051\",\"W052\",\"W053\",\"W054\",\"W055\",\"W056\",\"W057\",\"H058\",\"H059\",\"H060\",\"H061\",\"H062\",\"H063\",\"H064\",\"H065\",\"H066\",\"H067\",\"H068\",\"H069\",\"H070\",\"S071\",\"S072\",\"S073\",\"W074\",\"W075\",\"W076\",\"W077\",\"W078\",\"W079\",\"H080\",\"H081\",\"H082\",\"H083\",\"H084\",\"S085\",\"S086\",\"S087\",\"S088\",\"S089\",\"S090\",\"S091\",\"H092\",\"H093\",\"H094\",\"H095\",\"H096\",\"H097\",\"B098\",\"B099\",\"B100\",\"B101\",\"B102\",\"S103\",\"S104\",\"S105\",\"S106\",\"S107\",\"H108\",\"H109\",\"H110\",\"H111\",\"H112\",\"H113\",\"S114\",\"S115\",\"S116\",\"S117\",\"S118\",\"S119\",\"H120\",\"H121\"";
		addToLinkedList(list);
		String shirtURL="/images/shirt/"+s.get(shirtCount)+".png";
		String bodyURL="/images/body/"+b.get(bodyCount)+".png";
		String headURL="/images/head/"+h.get(headCount)+".png";
		String weaponURL="/images/weapon/"+w.get(weaponCount)+".png";
		String bgURL="";
		loadBufferedImage(headURL,bodyURL,shirtURL,weaponURL,bgURL);
		g.drawImage(background, 80, 50, this);
		g.drawImage(body, 80, 50, this);
		g.drawImage(head, 80, 40, this);
		g.drawImage(shirt, 80, 50, this);
		g.drawImage(weapon, 70, 50, this);
	}



}