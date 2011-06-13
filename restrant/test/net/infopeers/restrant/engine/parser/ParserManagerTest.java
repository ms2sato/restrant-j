package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.TestParams;
import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;
import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;
import net.infopeers.restrant.util.GoogleCollectionExtensionMultimapFactory;

public class ParserManagerTest extends TestCase {


	private PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
	private ExtensionMultimapFactory exPolicy = new GoogleCollectionExtensionMultimapFactory();

	public void testSenario() throws Exception {

		List<PatternParser> parsers = new ArrayList<PatternParser>();
		TextUrlParser index = new TextUrlParser(
				"/ :action=index", phFormatter);
		parsers.add(index);
		
		TextUrlParser getter = new TextUrlParser(
				"/:controller?id=:id :action=get", phFormatter);
		parsers.add(getter);

		TextUrlParser byEditor = new TextUrlParser(
				"/:controller?editor=:editor :action=getByEditor", phFormatter);
		parsers.add(byEditor);

		TextUrlParser byRel = new TextUrlParser(
				"/:controller?mode=relation :action=getByRelation", phFormatter);
		parsers.add(byRel);

		TextUrlParser content = new TextUrlParser(
				"/:controller?content=:content&comment=:comment :action=post",
				phFormatter);
		parsers.add(content);

		TextUrlParser withDot = new TextUrlParser(
				"/:controller/:id.json :action=get",
				phFormatter);
		parsers.add(withDot);

		TextUrlParser perform = new TextUrlParser(
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
			pm.select("/");
			
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
			pm.select("/contents");
			
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
			pm.select("/contents");
			
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
			pm.select("/contents");
			
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
			pm.select("/contents");
			
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
			pm.select("/contents/1111.json");
			
			//withDotがコールされる
			assertEquals(withDot, pm.getSelectedParser());
			
			Params params = pm.getSelectedParam();
			assertNull(params.get("id.json"));
			assertEquals("1111", params.get("id"));
		}
	}
}
