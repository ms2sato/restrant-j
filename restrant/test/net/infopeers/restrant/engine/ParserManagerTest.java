package net.infopeers.restrant.engine;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.TestParams;

public class ParserManagerTest extends TestCase {


	private DefaultPlaceholderFormatter phFormatter = new DefaultPlaceholderFormatter();
	private ExtensionPolicy exPolicy = new GoogleCollectionExtensionPolicy();

	public void testSenario() throws Exception {

		List<Parser> parsers = new ArrayList<Parser>();
		TextParser index = new TextParser(
				"/ :action=index", phFormatter);
		parsers.add(index);
		
		TextParser getter = new TextParser(
				"/:controller?id=:id :action=get", phFormatter);
		parsers.add(getter);

		TextParser byEditor = new TextParser(
				"/:controller?editor=:editor :action=getByEditor", phFormatter);
		parsers.add(byEditor);

		TextParser byRel = new TextParser(
				"/:controller?mode=relation :action=getByRelation", phFormatter);
		parsers.add(byRel);

		TextParser content = new TextParser(
				"/:controller?content=:content&comment=:comment :action=post",
				phFormatter);
		parsers.add(content);

		TextParser withDot = new TextParser(
				"/:controller/:id.json :action=get",
				phFormatter);
		parsers.add(withDot);

		TextParser perform = new TextParser(
				"/:controller :action=perform", phFormatter);
		parsers.add(perform);

		//インデクス
		{
			ParserManager pm = new ParserManager(exPolicy) {

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
			ParserManager pm = new ParserManager(exPolicy) {

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
			ParserManager pm = new ParserManager(exPolicy) {

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
			ParserManager pm = new ParserManager(exPolicy) {

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
			ParserManager pm = new ParserManager(exPolicy) {

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
			ParserManager pm = new ParserManager(exPolicy) {

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
