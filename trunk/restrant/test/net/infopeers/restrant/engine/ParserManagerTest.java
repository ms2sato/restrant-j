package net.infopeers.restrant.engine;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.TestParams;

public class ParserManagerTest extends TestCase {


	private DefaultPlaceholderFormatter phFormatter = new DefaultPlaceholderFormatter();

	public void testSenario() throws Exception {

		List<Parser> parsers = new ArrayList<Parser>();
		DefaultParser index = new DefaultParser(
				"/ :action=index", phFormatter);
		parsers.add(index);
		
		DefaultParser getter = new DefaultParser(
				"/:controller?id=:id :action=get", phFormatter);
		parsers.add(getter);

		DefaultParser byEditor = new DefaultParser(
				"/:controller?editor=:editor :action=getByEditor", phFormatter);
		parsers.add(byEditor);

		DefaultParser byRel = new DefaultParser(
				"/:controller?mode=relation :action=getByRelation", phFormatter);
		parsers.add(byRel);

		DefaultParser content = new DefaultParser(
				"/:controller?content=:content&comment=:comment :action=post",
				phFormatter);
		parsers.add(content);

		DefaultParser withDot = new DefaultParser(
				"/:controller/:id.json :action=get",
				phFormatter);
		parsers.add(withDot);

		DefaultParser perform = new DefaultParser(
				"/:controller :action=perform", phFormatter);
		parsers.add(perform);

		//インデクス
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					return params;
				}
			};

			pm.setParsers(parsers);
			pm.execute("/");
			
			//indexがコールされる
			assertEquals(index, pm.getSelectedParser());
		}
		
		
		//editorに値が入っている場合
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					params.addParams("editor", "testman");
					return params;
				}
			};

			pm.setParsers(parsers);
			pm.execute("/contents");
			
			//byEditorがコールされる
			assertEquals(byEditor, pm.getSelectedParser());
		}

		//content、commentに値が入っている場合
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					params.addParams("content", "testcontent");
					params.addParams("comment", "");
					return params;
				}
			};
			
			pm.setParsers(parsers);
			pm.execute("/contents");
			
			//contentがコールされる
			assertEquals(content, pm.getSelectedParser());
		}

		//editorに値が入っていて、他の値も存在する場合
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					params.addParams("editor", "testman");
					params.addParams("aaa", "bbb");
					return params;
				}
			};

			pm.setParsers(parsers);
			pm.execute("/contents");
			
			//editorがコールされる
			assertEquals(byEditor, pm.getSelectedParser());
		}

		
		//パラメータが無い場合
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					return params;
				}
			};

			pm.setParsers(parsers);
			pm.execute("/contents");
			
			//performがコールされる
			assertEquals(perform, pm.getSelectedParser());
		}
	
		//ドットの含まれているケース
		{
			ParserManager pm = new ParserManager() {

				EditableParams createParams() {
					TestParams params = new TestParams();
					return params;
				}
			};

			pm.setParsers(parsers);
			pm.execute("/contents/1111.json");
			
			//withDotがコールされる
			assertEquals(withDot, pm.getSelectedParser());
			
			Params params = pm.getSelectedParam();
			assertNull(params.get("id.json"));
			assertEquals("1111", params.get("id"));
		}
	}
}