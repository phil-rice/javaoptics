<%@ taglib prefix="profile" uri="/WEB-INF/profile.tld" %>

<% pageContext.setAttribute("profiler", one.xingyi.profilingJsp.ProfilerForTests.profiler);%>

<profile:profile id="profileTag" profiler="${profiler}">
    <profile:delay/>
</profile:profile>

<profile:start id="startEndProfileTag" profiler="${profiler}"/>
<profile:delay/>
<profile:end id="startEndProfileTag" profiler="${profiler}"/>
