import com.seventhree.st.model.core.PdmParser
import com.seventhree.st.model.springmvc.Model2SpringMVCConverter

def file = './crm.pdm'
def pageName = 'digitizing'
def p = new PdmParser(file: file)
p.parse()

scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
targetDir = new File(scriptDir, '../../../target/').toString();
controller_tpl = new File(scriptDir, 'springMVCTemplates/DemoController.tpl').toString()
service_tpl = new File(scriptDir, 'springMVCTemplates/DemoService.tpl').toString()
service_impl_tpl = new File(scriptDir, 'springMVCTemplates/DemoServiceImpl.tpl').toString()
//vo_tpl = new File(scriptDir, 'springMVCTemplates/DemoVO.tpl').toString()

def msm = new Model2SpringMVCConverter(
        pageName:pageName,
        targetDir: targetDir,
        controller_tpl: controller_tpl,
        service_tpl: service_tpl,
        service_impl_tpl: service_impl_tpl,
//        vo_tpl: vo_tpl
)
msm.run(p.table_column_infos)
