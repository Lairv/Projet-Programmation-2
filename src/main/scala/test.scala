import swing._

object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "MyApplication"
		contents = new BorderPanel {
			add(new Label("En haut"), BorderPanel.Position.North)
			add(new Label("Au milieu"), BorderPanel.Position.Center)
			add(new Label("En bas"), BorderPanel.Position.South)		
		}
	}
}